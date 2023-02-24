package space.nixus.pubtrans.controller;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
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


@RestController
@SecurityRequirement(name = "bearer")
public class RouteController {

    @Autowired
    private RouteService routeService;

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    @PostMapping("/routes/query")
    List<Route> queryRoute(@RequestBody RouteQuery query) {
        throw new NotImplementedException();
    }

    @PostMapping("/routes/favor/{id}")
    String favorRoute(@PathVariable("id") Long id) {
        var user = getUser();
        throw new NotImplementedException();
    }

    @DeleteMapping("/routes/unfavor/{id}")
    String unfavorRoute(@PathVariable("id") Long id) {
        throw new NotImplementedException();
    }

    @GetMapping("/alerts/list")
    List<Alert> listAlerts() {
        throw new NotImplementedException();
    }

    @PostMapping("/alerts/add")
    Alert addAlert(@RequestBody AlertParam param) {
        throw new NotImplementedException();
    }

    @PutMapping("/alerts/update/{id}")
    Alert updateAlert(@RequestBody AlertParam param) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/alerts/delete/{id}")
    Alert deleteAlert(@RequestBody AlertParam param) {
        throw new NotImplementedException();
    }

}
