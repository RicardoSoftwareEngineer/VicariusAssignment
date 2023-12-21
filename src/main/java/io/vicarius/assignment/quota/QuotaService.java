package io.vicarius.assignment.quota;

import io.vicarius.assignment.user.UserDTO;
import io.vicarius.assignment.user.UserMessages;
import io.vicarius.assignment.user.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuotaService {
    @Autowired
    UserService userService;
    private static Integer QUOTA_LIMIT = 5;
    private Map<String, Integer> quotaCache = new HashMap<>();

    public String consume(String id){
        if(!quotaCache.containsKey(id)){
            quotaCache.put(id, 0);
        }
        if(quotaCache.get(id) == QUOTA_LIMIT){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, QuotaMessages.QUOTA_LIMIT_REACHED);
        }
        quotaCache.put(id, quotaCache.get(id)+1);
        return "Total requests remaining for this user: " + (QUOTA_LIMIT - quotaCache.get(id));
    }

    public List<UserDTO> getUsersQuota(){
        List<UserDTO> userDTOS = userService.retrieve();
        for(UserDTO userDTO: userDTOS){
            if(quotaCache.containsKey(userDTO.getId())){
                userDTO.setRequestsRemaining(QUOTA_LIMIT - quotaCache.get(userDTO.getId()));
            }else{
                userDTO.setRequestsRemaining(QUOTA_LIMIT);
            }
        }
        return userDTOS;
    }
}
