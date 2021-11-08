package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserRequest;
import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserResponse;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/super-users")
public class SuperUserController {
    private final SuperUserService superUserService;

    private final EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser;
    private final EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse;

    @Autowired
    public SuperUserController(SuperUserService superUserService,
                               EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser,
                               EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse) {
        this.superUserService = superUserService;
        this.createSuperUserToSuperUser = createSuperUserToSuperUser;
        this.superUserToCreateSuperUserResponse = superUserToCreateSuperUserResponse;
    }

    // PRE AUTHORIZE (ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSuperUserResponse createSuperUser(@RequestBody @Valid CreateSuperUserRequest request) {
        var superUser = createSuperUserToSuperUser.convert(request);
        var result = superUserService.create(superUser);
        return superUserToCreateSuperUserResponse.convert(result);
    }
}
