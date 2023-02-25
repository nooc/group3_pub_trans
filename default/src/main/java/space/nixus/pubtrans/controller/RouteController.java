package space.nixus.pubtrans.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import space.nixus.pubtrans.model.Alert;
import space.nixus.pubtrans.model.AlertParam;
import space.nixus.pubtrans.model.Route;
import space.nixus.pubtrans.model.RouteQuery;
import space.nixus.pubtrans.model.User;
import space.nixus.pubtrans.service.RouteService;
import space.nixus.pubtrans.error.*;

@RestController
@SecurityRequirement(name = "bearer")
public class RouteController {

    @Autowired
    private RouteService routeService;

    private User getUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            var details = auth.getDetails();
            if(details != null) {
                return (User)details;
            }
        }
        throw new UserNotFoundError();
    }

    @PostMapping("/routes/query")
    List<Route> queryRoute(@RequestBody RouteQuery query) {
        return routeService.queryRoute(query);
    }

    @PostMapping("/routes/favor/{id}")
    String favorRoute(@PathVariable("id") Long id) {
        if(routeService.favorRoute(getUser().getId(), id)) {
            return "OK";
        }
        throw new RouteNotFoundError();
    }

    @DeleteMapping("/routes/unfavor/{id}")
    String unfavorRoute(@PathVariable("id") Long id) {
        if(routeService.unfavorRoute(id)) {
            return "OK";
        }
        throw new RouteNotFoundError();
    }

    @GetMapping("/alerts/list")
    List<Alert> listAlerts() {
        return routeService.listAlerts();
    }

    @PostMapping("/alerts/add")
    Alert addAlert(@RequestBody AlertParam param) {
        return routeService.addAlert(param);
    }

    @PutMapping("/alerts/update/{id}")
    Alert updateAlert(@PathVariable("id") Long id, @RequestBody AlertParam param) {
        return routeService.updateAlert(id, param);
    }

    @DeleteMapping("/alerts/delete/{id}")
    String deleteAlert(@PathVariable("id") Long id) {
        if(routeService.deleteAlert(id)) {
            return "OK";
        }
        throw new AlertNotFoundError();
    }

}
