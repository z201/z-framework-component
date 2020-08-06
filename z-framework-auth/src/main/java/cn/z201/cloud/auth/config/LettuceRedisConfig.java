package cn.z201.cloud.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@EnableCaching
public class LettuceRedisConfig {

	private String host;
	private String password;
	private int port;
	private int timeout;
	private int database;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Bean(name = "LettuceFactory")
	public LettuceConnectionFactory lettuceConnectionFactory() {
		LettuceConnectionFactory cf = new LettuceConnectionFactory();
		cf.getStandaloneConfiguration().setDatabase(database);
		cf.getStandaloneConfiguration().setHostName(host);
		cf.getStandaloneConfiguration().setPort(port);
		cf.setValidateConnection(true);
		if (password != null && password.length() > 0) {
			cf.getStandaloneConfiguration().setPassword(RedisPassword.of(password));
		}
		return cf;
	}

	@Bean(name = "LettuceRedisConfig")
	public RedisTemplate<String, Object> redisTemplate(@Qualifier(value = "LettuceFactory") LettuceConnectionFactory cf) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(cf);
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		redisTemplate.setValueSerializer(redisSerializer);
		return redisTemplate;
	}

}
