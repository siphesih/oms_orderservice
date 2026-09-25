package fnb.oms_orderservice.security;

// Principal placed in the SecurityContext by JwtAuthenticationFilter.
// customerId comes from the signed JWT, never from the request body or URL.
public record AuthenticatedUser(Long customerId, String email) {
}
