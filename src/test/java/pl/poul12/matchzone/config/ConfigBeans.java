package pl.poul12.matchzone.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.poul12.matchzone.repository.*;
import pl.poul12.matchzone.service.*;
import pl.poul12.matchzone.util.MailSender;

@Configuration
public class ConfigBeans {

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    CommentRepository commentRepository(){
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    public RoleRepository roleRepository(){
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    public PersonalDetailsRepository personalDetailsRepository() {
        return Mockito.mock(PersonalDetailsRepository.class);
    }

    @Bean
    public AppearanceRepository appearanceRepository(){
        return Mockito.mock(AppearanceRepository.class);
    }

    @Bean
    public MessageRepository messageRepository(){
        return Mockito.mock(MessageRepository.class);
    }

    @Bean
    public MailSender mailSender(){
        return Mockito.mock(MailSender.class);
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceImpl(commentRepository(), Mockito.mock(UserService.class));
    }

    @Bean
    public PersonalDetailsService personalDetailsService(){
        return new PersonalDetailsServiceImpl(personalDetailsRepository());
    }

    @Bean
    public AppearanceService appearanceService(){
        return new AppearanceServiceImpl(appearanceRepository());
    }

    @Bean
    public MessageService messageService(){
        return new MessageServiceImpl(messageRepository(), Mockito.mock(UserService.class));
    }

}
