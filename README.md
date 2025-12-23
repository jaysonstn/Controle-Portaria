# Projeto Controle de Portaria

![Tela Controle de Portaria](./Controle-Portaria.png)


### Getting Started


Compilar com:
``` 
    javac --module-path lib/javafx-sdk-25.0.1/lib \
      --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
      -cp ".;lib/sqlite-jdbc-3.51.1.0.jar" \
      -d bin \
      src/br/com/portaria/*.java src/br/com/portaria/model/*.java src/br/com/portaria/dao/*.java
```

Rodar com:
```
    java --module-path lib/javafx-sdk-25.0.1/lib \
     --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
     -cp ".;lib/sqlite-jdbc-3.51.1.0.jar;bin" \
     br.com.portaria.ControlePortariaApp
```

---
### Requisitos do projeto:
* Java 25 LTS
* Java FX 25.0.1 LTS
* sqlite-jdbc-3.51.1.0

