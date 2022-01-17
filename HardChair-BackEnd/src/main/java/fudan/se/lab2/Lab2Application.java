package fudan.se.lab2;

import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;

/**
 * Welcome to 2020 Software Engineering Lab2.
 * This is your first lab to write your own code and build a spring boot application.
 * Enjoy it :)
 *
 * @author LBW
 */
@SpringBootApplication
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ResultRepository resultRepository;

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.
                // Create an admin if not exists.
                if (userRepository.findByUsername("admin") == null) {
                    HashSet<Authority> set = new HashSet<>();
                    User admin = new User(
                            "admin",
                            passwordEncoder.encode("password"),
                            "libowen",
                            "libowen@fudan.edu.cn",
                            "Fudan University",
                            "China",
                            set
                    );
                    userRepository.save(admin);
                }


            }

        };
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}

