package pl.poul12.matchzone.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.poul12.matchzone.repository.AppearanceRepository;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;
import pl.poul12.matchzone.repository.RoleRepository;
import pl.poul12.matchzone.repository.UserRepository;
import pl.poul12.matchzone.util.MailSender;

@Configuration
public class ConfigBeans {

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
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
    public MailSender mailSender(){
        return Mockito.mock(MailSender.class);
    }

}
