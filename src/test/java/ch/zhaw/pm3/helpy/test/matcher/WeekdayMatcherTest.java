package ch.zhaw.pm3.helpy.test.matcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import ch.zhaw.pm3.helpy.matcher.strategy.Weekday;
import ch.zhaw.pm3.helpy.model.job.Job;
import ch.zhaw.pm3.helpy.model.user.User;
import ch.zhaw.pm3.helpy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeekdayMatcherTest {

    private static final String TEST_DATE = "2020-10-14";
    private static Weekday weekdayMatcher;
    private static final Set<String> expectedUsersByEmail = new HashSet<>(Arrays.asList(
            "leandro@email.com",
            "hawkeye@email.com",
            "spidey@email.com"
    ));
    private Set<User> expectedUsers;

    @Mock
    Job job;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        weekdayMatcher = new Weekday();
        weekdayMatcher.setUserRepository(userRepository);
        expectedUsers = initExpectedUsers();
    }

    @Test
    void contextLoads() {
        assertNotNull(userRepository);
    }

    @Test
    void testMatch() {
        when(job.getDueDate()).thenReturn(LocalDate.parse(TEST_DATE));
        Collection<User> result = weekdayMatcher.getPotentialHelpers(job);
        assertIterableEquals(expectedUsers, result);
    }

    private Set<User> initExpectedUsers() {
        Set<User> expectedUsers = new HashSet<>();
        for(String userEmail : expectedUsersByEmail) {
            expectedUsers.add(userRepository.findById(userEmail).get());
        }
        return expectedUsers;
    }
}
