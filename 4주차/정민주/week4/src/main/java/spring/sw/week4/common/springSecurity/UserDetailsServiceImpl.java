package spring.sw.week4.common.springSecurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import spring.sw.week4.domain.user.model.User;
import spring.sw.week4.domain.user.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // 이메일을 기반으로 사용자 로드
        User user = userRepository.findByEmail(identifier)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with subject: " + identifier));

        return new UserDetailsImpl(user.getEmail(), "user");

    }

}
