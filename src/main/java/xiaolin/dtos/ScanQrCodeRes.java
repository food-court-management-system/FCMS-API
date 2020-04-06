package xiaolin.dtos;

import lombok.Data;

@Data
public class ScanQrCodeRes {
    Long userId;
    String email;
    String provider;
    Long walletId;
    Float balance;
}
