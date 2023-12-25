package io.vicarius.assignment.quota;

import io.vicarius.assignment.user.UserDTO;
import io.vicarius.assignment.user.UserMessages;
import io.vicarius.assignment.user.UserRepository;
import io.vicarius.assignment.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class QuotaServiceImpl implements QuotaService{
    @Autowired
    UserServiceImpl userService;
    @Autowired
    QuotaRepository quotaRepository;
    @Autowired
    UserRepository userRepository;
    private static Integer QUOTA_LIMIT = 5;

    public String consume(String id){
        int totalRequestsMade;
        Optional<Quota> quotaOptional = quotaRepository.findById(id);

        if(quotaOptional.isEmpty()){
            userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, UserMessages.USER_NOT_FOUND));
            quotaRepository.save(new Quota(id, 1));
            totalRequestsMade = 1;
        }else{
            Quota quota = quotaOptional.get();
            if(quota.getTotalRequestsMade() == QUOTA_LIMIT){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, QuotaMessages.QUOTA_LIMIT_REACHED);
            }
            quota.setTotalRequestsMade(quota.getTotalRequestsMade()+1);
            quota = quotaRepository.save(quota);
            totalRequestsMade = quota.getTotalRequestsMade();
        }
        return "Total requests remaining for this user: " + (QUOTA_LIMIT - totalRequestsMade);
    }

    public List<UserDTO> getUsersQuota(){
        List<UserDTO> userDTOS = userService.retrieveAll();
        Optional<Quota> quota;
        for(UserDTO userDTO: userDTOS){
            quota = quotaRepository.findById(userDTO.getId());
            if(quota.isPresent()){
                userDTO.setRequestsRemaining(QUOTA_LIMIT - quota.get().getTotalRequestsMade());
            }else{
                userDTO.setRequestsRemaining(QUOTA_LIMIT);
            }
        }
        return userDTOS;
    }
}
