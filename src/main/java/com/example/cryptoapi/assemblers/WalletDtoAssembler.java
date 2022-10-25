package com.example.cryptoapi.assemblers;

import com.example.cryptoapi.controllers.UserController;
import com.example.cryptoapi.controllers.WalletController;
import com.example.cryptoapi.dtos.WalletDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WalletDtoAssembler implements SimpleRepresentationModelAssembler<WalletDto> {

    @Override
    public void addLinks(EntityModel<WalletDto> resource) {
        resource.add(linkTo(methodOn(WalletController.class)
                .getByUUID(Objects.requireNonNull(resource.getContent()).getWalletEntity().getId()))
                .withSelfRel());
        resource.add(linkTo(methodOn(WalletController.class)
                .getAllWallets()).withRel("all wallets"));

        List<String[]> ownersInfo = resource.getContent().getOwners()
                .stream().map(owner -> owner.split(" ")).collect(Collectors.toList());
        for (String[] ownerInfo : ownersInfo) {
            Long ownerIdentityNumber = Long.parseLong(ownerInfo[ownerInfo.length - 1]);
            resource.add(linkTo(methodOn(UserController.class)
                    .getUserByIdentityNumber(ownerIdentityNumber)).withRel("owners"));
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<WalletDto>> resources) {
        resources.add(linkTo(methodOn(WalletController.class).getAllWallets()).withSelfRel());
    }
}
