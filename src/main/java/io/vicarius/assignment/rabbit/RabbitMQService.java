package io.vicarius.assignment.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vicarius.assignment.rabbit.RabbitMQConfig;
import io.vicarius.assignment.user.UserDTO;
import io.vicarius.assignment.user.UserDocument;
import io.vicarius.assignment.user.UserElasticRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class RabbitMQService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    UserElasticRepository userElasticRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_UPDATE = "update";
    public static final String OPERATION_DELETE = "delete";


    public void sendCreateOperationToQueue(UserDTO userDTO){
        userDTO.setOperation(OPERATION_CREATE);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, userDTO);
    }

    public void sendUpdateOperationToQueue(UserDTO userDTO){
        userDTO.setOperation(OPERATION_UPDATE);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, userDTO);
    }

    public void sendDeleteOperationToQueue(UserDTO userDTO){
        userDTO.setOperation(OPERATION_DELETE);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, userDTO);
    }

    public boolean isDayTime(){
        // Get the current time in UTC
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalTime currentTime = utcTime.toLocalTime();

        // Define the time ranges
        LocalTime dayTimeStart = LocalTime.of(9, 0);   // 09:00 UTC
        LocalTime dayTimeEnd = LocalTime.of(17, 0);  // 17:00 UTC

        return currentTime.isAfter(dayTimeStart) && currentTime.isBefore(dayTimeEnd);
    }

    public void checkDatabaseSincronization(){
        UserDTO userDTO = (UserDTO) rabbitTemplate.receiveAndConvert(RabbitMQConfig.QUEUE);
        while(userDTO != null){
            switch (userDTO.getOperation()){
                case OPERATION_CREATE -> userElasticRepository.save(new UserDocument(userDTO));
                case OPERATION_UPDATE -> userElasticRepository.save(new UserDocument(userDTO));
                case OPERATION_DELETE -> userElasticRepository.deleteById(userDTO.getId());
            }
            userDTO = (UserDTO) rabbitTemplate.receiveAndConvert(RabbitMQConfig.QUEUE);
        }
    }
}

