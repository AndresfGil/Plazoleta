package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.api.IPlatoServicePort;
import com.pragma.powerup.domain.spi.IRestaurantePersistencePort;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import com.pragma.powerup.domain.spi.ITrazabilidadServicePort;
import com.pragma.powerup.domain.usecase.RestauranteUseCase;
import com.pragma.powerup.domain.usecase.PlatoUseCase;
import com.pragma.powerup.infrastructure.out.http.adapter.UsuarioHttpAdapter;
import com.pragma.powerup.infrastructure.out.http.adapter.MensajeriaHttpAdapter;
import com.pragma.powerup.infrastructure.out.http.adapter.TrazabilidadHttpAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestauranteJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.PlatoJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestauranteEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlatoEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlatoRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestauranteRepository;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IRestauranteRepository restauranteRepository;
    private final IRestauranteEntityMapper restauranteEntityMapper;
    private final IPlatoRepository platoRepository;
    private final IPlatoEntityMapper platoEntityMapper;
    private final AuthenticationService authenticationService;

    @Bean
    public IRestaurantePersistencePort restaurantePersistencePort() {
        return new RestauranteJpaAdapter(restauranteRepository, restauranteEntityMapper);
    }

    @Bean
    public IPlatoPersistencePort platoPersistencePort() {
        return new PlatoJpaAdapter(platoRepository, platoEntityMapper);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IUsuarioServicePort usuarioServicePort() {
        return new UsuarioHttpAdapter(restTemplate());
    }

    @Bean
    public IMensajeriaServicePort mensajeriaServicePort() {
        return new MensajeriaHttpAdapter(restTemplate());
    }

    @Bean
    public ITrazabilidadServicePort trazabilidadServicePort() {
        return new TrazabilidadHttpAdapter(restTemplate());
    }

    @Bean
    public IRestauranteServicePort restauranteServicePort() {
        return new RestauranteUseCase(restaurantePersistencePort(), usuarioServicePort());
    }

    @Bean
    public IPlatoServicePort platoServicePort() {
        return new PlatoUseCase(platoPersistencePort(), restauranteServicePort(), authenticationService);
    }
}