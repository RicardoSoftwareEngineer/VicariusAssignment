package io.vicarius.assignment.quota;

import io.vicarius.assignment.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuotaController {

    @Autowired
    QuotaServiceImpl quotaService;

    @PostMapping(value = "/quota/v1/consume/{id}")
    public String consumeQuota(@PathVariable String id){
        return quotaService.consume(id);
    }

    @GetMapping(value = "/quota/v1")
    public List<UserDTO> getUsersQuota(){
        return quotaService.getUsersQuota();
    }

}
