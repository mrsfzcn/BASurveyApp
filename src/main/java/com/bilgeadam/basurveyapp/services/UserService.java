package com.bilgeadam.basurveyapp.services;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtService;
import com.bilgeadam.basurveyapp.constant.ROLE_CONSTANTS;
import com.bilgeadam.basurveyapp.dto.request.AssignRoleToUserRequestDto;
import com.bilgeadam.basurveyapp.dto.request.UserUpdateRequestDto;
import com.bilgeadam.basurveyapp.dto.response.*;
import com.bilgeadam.basurveyapp.entity.Role;
import com.bilgeadam.basurveyapp.entity.User;
import com.bilgeadam.basurveyapp.entity.enums.State;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleAlreadyExistException;
import com.bilgeadam.basurveyapp.exceptions.custom.RoleNotFoundException;
import com.bilgeadam.basurveyapp.exceptions.custom.UndefinedTokenException;
import com.bilgeadam.basurveyapp.exceptions.custom.UserDoesNotExistsException;
import com.bilgeadam.basurveyapp.mapper.UserMapper;
import com.bilgeadam.basurveyapp.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleService roleService;

    private final StudentService studentService;
    private final TrainerService trainerService;

    private final ManagerService managerService;

    public UserService(UserRepository userRepository, @Lazy JwtService jwtService, @Lazy RoleService roleService, StudentService studentService, TrainerService trainerService, ManagerService managerService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.roleService = roleService;
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.managerService = managerService;
    }

    public List<ManagerResponseDto> getManagerList() {
        List<User> managers = userRepository.findManagers();

        return UserMapper.INSTANCE.toManagerResponseDto(managers);
    }

    public List<AdminResponseDto> getAdminList() {

        List<User> admins = userRepository.findAdmins();
        return UserMapper.INSTANCE.toAdminResponseDto(admins);
    }

    public Page<User> getUserPage(Pageable pageable) {

        return userRepository.findAllActive(pageable);
    }

    public User updateUser(String userEmail, UserUpdateRequestDto dto) {

        Optional<User> userToBeUpdated = userRepository.findActiveUserByEmail(userEmail);
        if (userToBeUpdated.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        userToBeUpdated.get().setEmail(dto.getEmail());
        userToBeUpdated.get().setAuthorizedRole(dto.getAuthorizedRole());
        return userRepository.save(userToBeUpdated.get());
    }

    /**
     * API'dan gelecek verilerin güncelleme işlemleri için hazırlandı.
     * @param userEmail Database'de kayıtlı olan email adresi
     * @param dto Güncellenecek bilgileri içeren nesne
     * @return Güncellenmiş User nesnesi.
     */
    public User updateTrainerWithApiData(String userEmail,UserUpdateRequestDto dto){
        Optional<User> userToBeUpdated = userRepository.findByEmail(userEmail);
        if (userToBeUpdated.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        userToBeUpdated.get().setFirstName(dto.getFirstName());
        userToBeUpdated.get().setLastName(dto.getLastName());
        userToBeUpdated.get().setEmail(dto.getEmail());
        userToBeUpdated.get().setAuthorizedRole(dto.getAuthorizedRole());
        userToBeUpdated.get().setState(State.ACTIVE);
        return userRepository.save(userToBeUpdated.get());
    }

    // Yeniden düzenlendi.
    public boolean deleteUser(Long userId) {

        Optional<User> userToBeDeleted = userRepository.findActiveById(userId);
        if (userToBeDeleted.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }

        if(userToBeDeleted.get().getRoles().stream().anyMatch(role -> role.getRole().equals("STUDENT"))){
            studentService.deleteStudentByUserOid(userToBeDeleted.get().getOid());
        }  if (userToBeDeleted.get().getRoles().stream().anyMatch(role -> role.getRole().equals("MASTER_TRAINER") || role.getRole().equals("ASSISTANT_TRAINER"))) {
            trainerService.deleteByTrainerOid(userToBeDeleted.get().getOid());
        }  if (userToBeDeleted.get().getRoles().stream().anyMatch(role -> role.getRole().equals("MANAGER"))) {
            managerService.deleteByManagerOid(userToBeDeleted.get().getOid());
        }
        return  true;
    }
    public boolean softDeleteTrainer(Long userId){
        Optional<User> userToBeDeleted = userRepository.findActiveById(userId);
        if (userToBeDeleted.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        trainerService.deleteTrainerByUser(userToBeDeleted.get());
        return   userRepository.softDeleteById(userToBeDeleted.get().getOid());
    }

    public UserSimpleResponseDto findByOid(Long userId) {

        Optional<User> userById = userRepository.findActiveById(userId);
        if (userById.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        return UserMapper.INSTANCE.toUserSimpleResponseDto(userById.get());
    }

    public UserSimpleResponseDto findByEmailToken(String token) {
        Optional<Long> userIdOptional = jwtService.getUserIdFromToken(token);
        if (userIdOptional.isEmpty()) {
            throw new UndefinedTokenException("Invalid token.");
        }
        Long userId = userIdOptional.get();
        System.out.println(userId);
        Optional<User> userById = userRepository.findActiveById(userId);
        if (userById.isEmpty()) {
            throw new UserDoesNotExistsException("User is not found");
        }
        return UserMapper.INSTANCE.toUserSimpleResponseDto(userById.get());
    }

    public List<UserTrainersAndStudentsResponseDto> getTrainersAndStudentsList(String jwtToken) {
        Optional<User> user = userRepository.findActiveUserByEmail(jwtService.extractEmail(jwtToken));
        if (user.isEmpty()) throw new UserDoesNotExistsException("User is not found");
        if (!roleService.userHasRole(user.get(), ROLE_CONSTANTS.ROLE_MANAGER)) throw new AccessDeniedException("Unauthorized account");

        List<User> trainersWithStudent = userRepository.findTrainersAndStudents();

        return UserMapper.INSTANCE.toUserTrainersAndStudentsResponseDto(trainersWithStudent);
    }


    public Boolean assignRoleToUser(AssignRoleToUserRequestDto request) {

        Optional<User> user = userRepository.findActiveUserByEmail(request.getUserEmail());
        if (user.isEmpty()) throw new UserDoesNotExistsException("User is not found");
        if(!roleService.hasRole(request.getRole()))  throw new RoleNotFoundException("Role is not found");
        if(roleService.userHasRole(user.get(), request.getRole())) throw new RoleAlreadyExistException("Role already exist");
        Role role = roleService.findActiveRole(request.getRole());
        user.get().getRoles().add(role);
        role.getUsers().add(user.get());
        userRepository.save(user.get());
        roleService.save(role);

        return true;
    }

    public Optional<User> findActiveById(Long userOid) {

        return userRepository.findActiveById(userOid);
    }

    public Optional<User> findByEmail(String extractEmail) {

        return userRepository.findActiveUserByEmail(extractEmail);
    }


    public User save(User auth) {
        return userRepository.save(auth);
    }

    public Optional<User> findById(Long id) {
        return findById(id);
    }

    public List<FindAllUserDetailsResponseDto> findAllUserDetails() {
        List<User> userList = userRepository.findAllByRolesNotADMIN();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(userList.isEmpty()) throw new UserDoesNotExistsException("Kayıtlı User Bulunamadı");
        List<FindAllUserDetailsResponseDto> findAllUserDetailsResponseDtoList = userList.stream().map(user ->{
                    FindAllUserDetailsResponseDto dto = UserMapper.INSTANCE.toFindAllUserDetailsResponseDto(user);
                    String date = dateFormat.format(user.getCreatedAt());

                    dto.setCreatedDate(date);
                    return dto;
                }
        ).collect(Collectors.toList());
        return findAllUserDetailsResponseDtoList;
    }

    public Optional<User> findByApiId(String apiId){
        return userRepository.findByApiId(apiId);
    }

    public List<User> findByApiIdContainsAndState(String keyword, State state){
        return userRepository.findByApiIdContainsAndState(keyword,state);
    }


}
