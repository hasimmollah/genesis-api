package com.hasim.genesis.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailResponse {
	@JsonProperty("email")
    private String email;
	@JsonProperty("userId")
    private String userId;

}
