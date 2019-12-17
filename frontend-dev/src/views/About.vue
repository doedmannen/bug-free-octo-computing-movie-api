<template>
  <div class="about">
    <h1>What is JWT?</h1>
    <p>
      Json web tokens or JWT is an access token used to authenticate a user on a service. Once the users has successfully logged in on the server, they are given a token to use as authentication with the services. 
    </p>
    <p>
      The token is issued by a token provider using a signature with a secret key. Depending on how the token is signed, other services can verify that it was signed by the token provider without knowing the secret key used to sign it (provided that they are given a public key for verification purposes). 
    </p>
    <p>
      A JWT consists of three parts. A header, a payload and a signature. The three parts are separated with dots (header.payload.signature). The header and payload are only encoded with base64 and are easy for anyone to decode. The header and payload should therefore not contain any sensitive information about the user. 
    </p>
    <p>
      The header usually contains the type of token (JWT) and what algoritm was used to sign it. The payload can contain any information the provider sees fit, but should not contain any sensitive information since it is very easy to decode the payload. Usually it contains some identification for who the JWT belongs to (user id or name), what roles or rights the user has on the servers, what time the JWT was issued and at what time the JWT stops being valid. Both the header and payload follow the JSON format. 
    </p>
    <p>
      The last part is a signature. The signature is produced by hashing or encoding the header and payload with a secret key that only the JWT provider knows. This is done to prevent others from tampering with the token, trying to change the information of the payload (like elevating the users rights on the service to that of an admin). 
    </p>
    <p>
      Since only the server providing the tokens knows the secret key used in the signature it is easy to verify that the token is signed correctly. If the provider leaks the secret key, anyone could sign a JWT making it look as a valid token from the provider. It is vital that the secret key is kept secret. 
    </p>
  </div>
</template>

<script>
export default {
  name: "about",

  components: {
    //
  },

  data: () => ({
    //
  })
};
</script>

<style scoped>
</style>
