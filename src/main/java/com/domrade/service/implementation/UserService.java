package com.domrade.service.implementation;

import com.domrade.service.interfaces.IUserService;
import com.domrade.service.interfaces.BaseService;
import com.domrade.domain.Friend;
import com.domrade.domain.Role;
import com.domrade.domain.User;
import com.domrade.repository.IUserRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseService<User> implements IUserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private static final String PROFILE_PICTURE_PATH_ONE = "src/main/resources/static/";
    private static final String PROFILE_PICTURE_PATH_NIX = "/resources/static";

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User get(Long id) {
        //   return userRepository.findOne(id);
        Optional<User> aUser = userRepository.findById(id);

        return aUser.get();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public List<Role> getAllRoles(User user) {
        return userRepository.getUserRoles(user.getId());
    }

    @Override
    public List<Role> getUserRole(String email) {
        return userRepository.getUserRoles(email);
    }
    
    @Override
    public void setUserRole(Role role, String emailAddress) {
        User tempUser = userRepository.findOneByEmail(emailAddress);
        tempUser.setRole(role);
        save(tempUser);
    }

    // Get pending join requests by the network id
    @Override
    public List<User> getPendingJoinRequests(long id) {
        return userRepository.getPendingJoinRequests(id);
    }

    @Override
    public User save(User user) {
        userRepository.save(user);

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> retValues = new ArrayList<>();
        Iterable<User> iterable = userRepository.findAll();
        iterable.forEach(retValues::add);
        
        return retValues;
    }
    @Override
    public Long getLoggedInUserId() {
        Object aPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (aPrincipal instanceof UserDetails) {
            UserDetails aUserDetails = (UserDetails) aPrincipal;
            User aUser = getByEmail(aUserDetails.getUsername());
            LOGGER.log(Level.INFO, aUser.getEmail() + " is logged in");

            return aUser.getId();
        }
        return -1L;
    }

    @Override
    public String getLoggedInEmailAddress() {
        Object aPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (aPrincipal instanceof UserDetails) {
            String email = ((UserDetails) aPrincipal).getUsername();
            return email;
        }
        return "error";
    }

    @Override
    public User findById(long userId) {
        Optional<User> aUser = userRepository.findById(userId);
        return aUser.get();
    }

    /**
     * Gets the remote address from a HttpServletRequest object. It prefers the
     * `X-Forwarded-For` header, as this is the recommended way to do it (user
     * may be behind one or more proxies).
     *
     * Taken from https://stackoverflow.com/a/38468051/778272 Taken from
     * https://stackoverflow.com/questions/12324466/finding-user-ip-address
     *
     * @param request - the request object where to get the remote address from
     * @return a string corresponding to the IP address of the remote machine
     */
    @Override
    public String getUserIpAddress() {
        HttpServletRequest httpServletRequest
                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = httpServletRequest.getRemoteAddr();
        LOGGER.log(Level.INFO, getLoggedInEmailAddress() + " is logged in from IP Address " + ipAddress);

        return ipAddress;
    }

    @Transactional
    @Override
    public void saveUserProfilePicture(UploadedFile profilePicture, long userId) {
        User aUser = get(userId);
        boolean nix = Boolean.TRUE;
        boolean windows = Boolean.FALSE;
        String userFirstName = get(userId).getFirstName();
        char firstLetterOfFirstName = userFirstName.charAt(0);
        String oS = System.getProperty("os.name");
        String token = " ";
        String oSElements[] = oS.split(token);
        String oSName = oSElements[0];
        if (oSName.equals("Windows")) {
            windows = Boolean.TRUE;
            nix = Boolean.FALSE;
        } else {
            windows = Boolean.FALSE;
            nix = Boolean.TRUE;
        }

        LOGGER.log(Level.INFO, "OS: " + oS);

        try {
            byte[] thePicture = profilePicture.getContents();
            BufferedImage bImage = null;
            bImage = ImageIO.read(profilePicture.getInputstream());
            // Windows
            Path folder1 = null;
            // Nix
            Path folderNix = null;

            if (nix) {
                File baseFolder = new File(PROFILE_PICTURE_PATH_NIX);
                //baseFolder.mkdir();
                folderNix = Paths.get(PROFILE_PICTURE_PATH_NIX + "/" + Character.toUpperCase(firstLetterOfFirstName));
                Path nixImageRoot = Paths.get(PROFILE_PICTURE_PATH_NIX);
                File nixImageRootDirectory = new File(nixImageRoot.toString());
                File folderNixForCurrentUser = new File(folderNix.toString());

                if (nixImageRootDirectory.exists()) {
                    LOGGER.log(Level.INFO, nixImageRootDirectory + " exists");
                }

                if (!(nixImageRootDirectory.exists()) && (nixImageRootDirectory.canWrite())) {
                    nixImageRootDirectory.mkdirs();
                } else if (!(nixImageRootDirectory.exists()) && (!(nixImageRootDirectory.canWrite()))) {
                    nixImageRootDirectory.setWritable(Boolean.TRUE);
                    nixImageRootDirectory.mkdir();
                }
                // Add the folder for the specific user based on the first letter of their first name
                if (!(folderNixForCurrentUser.exists())) {
                    if (folderNixForCurrentUser.canWrite()) {
                        boolean result = folderNixForCurrentUser.mkdir();
                        LOGGER.log(Level.INFO, "nix mkDir success? " + folderNixForCurrentUser.toString() + " " + result);
                    } else {
                        folderNixForCurrentUser.setWritable(Boolean.TRUE);
                        boolean result = folderNixForCurrentUser.mkdirs();
                        LOGGER.log(Level.INFO, "Folder creation successful? " + result);
                        LOGGER.log(Level.INFO, "nix mkDir success? Folder: " + folderNixForCurrentUser.toString() + " " + result);
                    }
                }
            }
            // Windows
            if (windows) {
                folder1 = Paths.get(PROFILE_PICTURE_PATH_ONE + "/" + Character.toUpperCase(firstLetterOfFirstName));
                File directory = new File(folder1.toString());
                LOGGER.log(Level.INFO, directory.getAbsolutePath());
                if (!(directory.exists()) && (directory.canWrite())) {
                    directory.mkdirs();
                }
                if (!(directory.exists())) {
                    if (directory.canWrite()) {
                        boolean result = directory.mkdirs();
                        LOGGER.log(Level.INFO, "mkDirs success? " + result);
                    } else {
                        directory.setWritable(Boolean.TRUE);
                        boolean result = directory.mkdirs();
                        LOGGER.log(Level.INFO, "mkDirs success? " + result);
                    }
                }
            }
            // Common
            String originalFileType = profilePicture.getContentType();
            String tokenTwo = "/";
            String[] fileTypeResultArray = originalFileType.split(tokenTwo);
            String fileExtension = fileTypeResultArray[1];
            String fileNameToBeSaved = get(userId).getEmail();
            // Windows
            if (windows) {
                Path file1 = Files.createTempFile(folder1, fileNameToBeSaved, "." + fileExtension);
                aUser.setProfilePicture(file1.toString());
                save(aUser);
                LOGGER.log(Level.INFO, "About to save image file to windows disk");
                ImageIO.write(bImage, fileExtension, new File(file1.toString()));
            }

            // Nix
            if (nix) {
                LOGGER.log(Level.INFO, "About to create file2 Path");
                Path file2 = Files.createTempFile(folderNix, fileNameToBeSaved, "." + fileExtension);
                LOGGER.log(Level.INFO, file2.toString());
                aUser.setProfilePicture(file2.toString());
                save(aUser);
                LOGGER.log(Level.INFO, "About to save image file to nix disk");
                ImageIO.write(bImage, fileExtension, new File(file2.toString()));
            }

        } catch (IOException io) {
            LOGGER.log(Level.ERROR, "Failed to save profile picture " + io.getLocalizedMessage());
        }
    }

    @Override
    public List<User> getUsersListFromFriends(List<Friend> theFriends) {
        List<User> retList = new ArrayList<>();
        User aUser;
        for (Friend f : theFriends) {
            if (!Objects.equals(f.getUserId(), getLoggedInUserId())) {
                aUser = this.findById(f.getUserId());
                retList.add(aUser);
            } else if (Objects.equals(f.getUserId(), getLoggedInUserId())) {
                aUser = this.findById(f.getRequesterId());
                retList.add(aUser);
            }            
        }
        return retList;
    }

    @Override
    public List<User> getNetworkMembersByNetworkId(long networkId, long userId) {
        return userRepository.getNetworkMembers(networkId, userId);
    }
    
    @Override
    // Get user objects from Friend object so users can be displayed
    public List<User> getUserFromFriendObject(List<Friend> sentList, User loggedInUser) {
        List<User> tempList = new ArrayList<>();
        User tempUser;
        for (Friend f : sentList) {
            // userId in Friend table is the user to whom the request was sent
            long tempUserId = f.getRequesterId();
            if (tempUserId != loggedInUser.getId()) {
                tempUser = findById(tempUserId);
                tempList.add(tempUser);
            } else {
                tempUserId = f.getUserId();
                tempUser = findById(tempUserId);
                tempList.add(tempUser);
            }
        }
        return tempList;
    }

    @Override
    public List<User> getNetworkMembersByNetworkId(long networkId) {
        return userRepository.getNetworkMembersByNetworkId(networkId);
    }

    @Override
    @Transactional
    public void setUserHasNotification(long userId) {
        User aUser = findById(userId);
        aUser.setHasNotification(Boolean.TRUE);
        userRepository.save(aUser);
    }
}
