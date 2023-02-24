package space.nixus.pubtrans.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.nixus.pubtrans.repository.RouteRepository;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;

    
}
