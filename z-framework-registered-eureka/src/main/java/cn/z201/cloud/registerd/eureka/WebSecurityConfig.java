package cn.z201.cloud.registerd.eureka;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 配置安全访问
 * 
 * @author 王正年
 *
 * @date 2019年2月21日
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		http.csrf().disable();
		// 注意：为了可以使用 http://${user}:${password}@${host}:${port}/eureka/
		// 这种方式登录,所以必须是httpBasic,
		// 如果是form方式,不能使用url格式登录
		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
	}

}