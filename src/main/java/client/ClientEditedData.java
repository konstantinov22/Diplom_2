package client;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Добавлен конструктор по умолчанию, если он необходим
public class ClientEditedData {
    private String newEmail;
    private String newPassword;
    private String newName;
}