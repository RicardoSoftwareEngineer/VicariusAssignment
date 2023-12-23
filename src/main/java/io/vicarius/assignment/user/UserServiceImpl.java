package io.vicarius.assignment.user;

import io.vicarius.assignment.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserElasticRepository userElasticRepository;
    @Autowired
    Util util;

    public UserDTO create(UserDTO userDTO) {
        //Only printing functions during nighttime
        if(!util.isDayTime())
            //throw new ResponseStatusException(HttpStatus.FORBIDDEN, UserMessages.DB_CHANGES_NOT_ALLOWED_AT_NIGHT);
        if(!StringUtils.hasText(userDTO.getFirstName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.MISSING_FIRST_NAME);
        if(!StringUtils.hasText(userDTO.getFirstName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.MISSING_LAST_NAME);
        if(userRepository.findByFirstNameAndLastName(userDTO.getFirstName(), userDTO.getLastName()).isPresent())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, UserMessages.USER_ALREADY_EXISTS);

        UserEntity userEntity = userRepository.save(new UserEntity(userDTO));
        userDTO.setId(userEntity.getId());
        util.sendCreateOperationToQueue(userDTO);
        return userDTO;
    }

    public List<UserDTO> retrieve(){
        if(util.isDayTime()){
            return userRepository.findAll()
                    .stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
        }else{
            util.checkDatabaseSincronization();
            return userElasticRepository.findAll()
                    .stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public UserDTO retrieveById(String id){
        if(util.isDayTime()){
            Optional<UserEntity> userEntity = userRepository.findById(id);
            if(userEntity.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, UserMessages.USER_NOT_FOUND);
            return new UserDTO(userEntity.get());
        }else{
            util.checkDatabaseSincronization();
            Optional<UserDocument> userDocument = userElasticRepository.findById(id);
            if(userDocument.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, UserMessages.USER_NOT_FOUND);
            return new UserDTO(userDocument.get());
        }
    }

    public UserDTO update(UserDTO userDTO, String id){
        if(!util.isDayTime())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, UserMessages.DB_CHANGES_NOT_ALLOWED_AT_NIGHT);
        if(!StringUtils.hasText(userDTO.getFirstName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.MISSING_FIRST_NAME);
        if(!StringUtils.hasText(userDTO.getFirstName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UserMessages.MISSING_LAST_NAME);

        if(userRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, UserMessages.USER_NOT_FOUND);
        if(userRepository.findByFirstNameAndLastName(userDTO.getFirstName(), userDTO.getLastName()).isPresent())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, UserMessages.USER_ALREADY_EXISTS);
        userDTO.setId(id);
        userRepository.save(new UserEntity(userDTO));
        util.sendUpdateOperationToQueue(userDTO);
        return userDTO;
    }

    public void delete(String id){
        if(!util.isDayTime())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, UserMessages.DB_CHANGES_NOT_ALLOWED_AT_NIGHT);
        if(userRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, UserMessages.USER_NOT_FOUND);
        userRepository.deleteById(id);
        util.sendUpdateOperationToQueue(new UserDTO(id));
    }
}
