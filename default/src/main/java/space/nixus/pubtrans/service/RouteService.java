package space.nixus.pubtrans.service;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.nixus.pubtrans.model.Alert;
import space.nixus.pubtrans.model.AlertParam;
import space.nixus.pubtrans.model.Route;
import space.nixus.pubtrans.model.RouteQuery;
import space.nixus.pubtrans.repository.AlertRepository;
import space.nixus.pubtrans.repository.RouteRepository;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;
    @Autowired
    AlertRepository alertRepository;

    public List<Route> queryRoute(RouteQuery query) {
        throw new NotImplementedException();
    }

    public boolean favorRoute(Long id) {
        throw new NotImplementedException();
    }

    public boolean unfavorRoute(Long id) {
        throw new NotImplementedException();
    }

    public List<Alert> listAlerts() {
        throw new NotImplementedException();
    }

    public Alert addAlert(AlertParam param) {
        throw new NotImplementedException();
    }

    
    public Alert updateAlert(Long id, AlertParam param) {
        throw new NotImplementedException();
    }

    public boolean deleteAlert(Long id) {
        throw new NotImplementedException();
    }
}
