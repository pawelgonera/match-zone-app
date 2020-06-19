package pl.poul12.matchzone.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import pl.poul12.matchzone.security.JwtProvider;
import pl.poul12.matchzone.service.*;
import pl.poul12.matchzone.util.MailSender;

@Configuration
public class ConfigControllerBeans {

    @Bean
    public MailSender mailSender(){
        return Mockito.mock(MailSender.class);
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return Mockito.mock(AuthenticationManager.class);
    }

    @Bean
    public JwtProvider jwtProvider(){
        return Mockito.mock(JwtProvider.class);
    }

    @Bean
    public PersonalDetailsService personalDetailsService(){
        return Mockito.mock(PersonalDetailsService.class);
    }

    @Bean
    public UserService userService(){
        return Mockito.mock(UserService.class);
    }

    @Bean
    public CommentService commentService(){
        return Mockito.mock(CommentService.class);
    }

    @Bean
    public AppearanceService appearanceService(){
        return Mockito.mock(AppearanceService.class);
    }

    @Bean
    public MessageService messageService(){
        return Mockito.mock(MessageService.class);
    }
}
