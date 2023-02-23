package space.nixus.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = App.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testList() throws Exception {
    }

    @Test
    public void testGet() throws Exception {
        
    }

    @Test
    public void testCreate() throws Exception {
        
    }

    @Test
    public void testUpdate() throws Exception {
        
    }

    @Test
    public void testDelete() throws Exception {
        
    }
}