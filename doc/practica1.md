# Documentación

### Listado de nuevas clases y métodos implementados

Clases:
- **UsuariosController**. Esta clase comenzó llamándose: "listadoUsuariosController", pero más adelante se le cambió el nombre para incluir más funciones relacionadas con los usuarios.

  - **listadoUsuarios:** Es la función encargada de listar a los usuarios registrados en el sistema, no sin antes comprobar que el usuario logueado es administrador. Está asociada al get:`/usuarios`

  - **descripciónUsuario:** Es la función que se encarga de mostrar los datos del usuario pedido. En caso de estar logueado con un usuario no administrador se accederá desde *Cuenta*, en la barra superior. 
En caso de estar logueado como administrador se accederá desde la lista de usuarios y desde *Cuenta* para ver los datos propios. Se llamará a la función al hacer get: `/usuarios/{id}` , donde {id} es el número de identificación del usuario descrito

  - **bloquearUsuario:** Esta función altera el estado del usuario indicado, pasando de *habilitado* a *bloqueado* y viceversa. Comprobará que el usuario que realiza está opción tiene las credenciales de administrador. A través de los form de cada usuario en la lista de usuarios. Al hacer post: `/usuarios/{id}/editar` con el id del usuario a bloquear o habilitar.
  

- **UsuarioNoAdminException:** Es la clase excepción que se lanza cuando un usuario logueado no administrador intenta realizar alguna acción solamente accesible al administrador.


### Listado de plantillas thyemeleaf añadidas

Plantillas:
- **listaUsuarios:** Es la plantilla asociada a la función *listadoUsuarios* y mostrará una tabla de los usuarios (menos el administrador) con el número id, el nombre, el correo, el botón para acceder a la descripción y el botón para bloquear/desbloquear.

- **descripcionUsuario:** La plantilla que muestra:
  - Id
  - Nombre
  - Email
  - Fecha de nacimiento
  - Estado habilitado/bloqueado (solo visible por el administrador)
  - Si es administrador (solo visible por el administrador)

- **fragments:** Aunque esta plantilla ya existía se le ha añadido el fragment de la *barra de menú*, que varía según credenciales:
  - ToDoList y Login en la página *about* de un usuario no logueado
  - ToDoList, Tareas y desplegable con Cuenta y Cerrar Sesión para usuarios logueados no administradores
  - ToDoList, Tareas, Lista de Usuarios desplegable con Cuenta y Cerrar Sesión para el usuario administrador.

- **resgistro:** A esta plantilla se le ha añadido, en el formulario, la checkbox para indicar que el nuevo usuario será administrador. Esta checkbox solamente aparecerá cuando no exista ningún administrador.

### Explicación de código fuente relevante de las nuevas funcionalidades implementadas

Algunas de las funcionalidades implementadas requerían de las siguientes modificaciones en el código fuente:

En **Usuario** los atributos:

```
private Boolean administrador;
private Boolean bloqueado;
```
Y las funciones getter y setter pertinentes:
```

public Boolean getAdministrador() { return administrador; }

public void setAdministrador(Boolean administrador) { this.administrador = administrador; }

public Boolean getBloqueado() { return bloqueado; }

public void setBloqueado(Boolean bloqueado) { this.bloqueado = bloqueado; }
```
Lo mismo en **RegistroData** en lo que respecta a *administrador*.

En **UsuarioService:**

Se ha añadido un estado más de Login para manejar el error de usuario bloqueado.
```
public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, USUARIO_BLOQUEADO}
```
Con su respectivo else en la función *login*:
```
else if (usuario.get().getBloqueado()){
            return LoginStatus.USUARIO_BLOQUEADO;
        }
```

Además, se ha añadido la función a la que llama *UsuarioController.bloquearUsuario*:
```
@Transactional
    public Usuario modificaEstadoUsuario(Long idUsuario, Boolean nuevoEstado) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new UsuarioServiceException("No existe usuario con id " + idUsuario);
        }
        usuario.setBloqueado(nuevoEstado);
        usuarioRepository.save(usuario);
        return usuario;
    }
```

Y una función que comprueba si hay algún usuario administrador ya registrado, para no mostrar la checkbox en el formulario de registro:
```
@Transactional(readOnly = true)
    public Boolean existeAdmin(){
        Boolean existe = false;
        for (Usuario usuario : usuarioRepository.findAll()) {
            if(!existe) existe = usuario.getAdministrador();
        }

        return existe;
    }
```

En **LoginController:**
Se maneja la respuesta de UsuarioService que envía cuando se intenta hacer login con un usuario bloqueado

```
else if (loginStatus == UsuarioService.LoginStatus.USUARIO_BLOQUEADO){
            model.addAttribute("error", "El usuario ha sido bloqueado");
            return "formLogin";
```
En la clase **ManagerUserSesion** se ha añadido una función para comprobar que hay un usuario logueado:
```
public void usuarioLogueado(HttpSession session){
        if(session.getAttribute("idUsuarioLogeado") == null){
            throw new UsuarioNoLogeadoException();
        }
    }
```
Y otras dos funciones para el administrador, uno para loguearlo (se ha preferido hacer en una función a parte de la del login normal) y otro para verificarlo.
```
public void logearAdmin(HttpSession session, Boolean administrador){
        session.setAttribute("administrador", administrador);
    }

    public void comprobarUsuarioAdministrador(HttpSession session, Boolean administrador){
        Boolean adminUsuarioLogueado = (Boolean) session.getAttribute("administrador");
        if (!adminUsuarioLogueado || adminUsuarioLogueado != administrador){
            throw new UsuarioNoAdminException();
        }
    }
```

En la base de datos **datos-dev.sql** y **datos-test.sql** se han añadido los valores de *administrador* y de *bloqueado*:
```
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', 'false', 'false');
```

### Tests de web implementados

Algunos de los tests implementados:

En **ListarUsuariosServiceTest**:
```
@Test
    @Transactional
    public void CrearListarBorrarYListarNuevoUsuario() throws Exception {
        // GIVEN
        Usuario usuario = new Usuario("atm64@alu");
        usuario.setNombre("Andres Tebar");
        usuario.setPassword("1234");
        usuario.setId(2L);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1996-12-19"));

        // WHEN
        usuarioRepository.save(usuario);
        List<Usuario> usuarios = usuarioService.allUsuarios();

        // THEN
        assertThat(usuarios.size()).isEqualTo(2);
        assertThat(usuarios.get(0).getNombre()).isEqualTo("Ana García");
        assertThat(usuarios.get(1).getNombre()).isEqualTo(usuario.getNombre());

        //AND
        usuarioRepository.delete(usuarios.get(1));
        usuarios = usuarioService.allUsuarios();
        assertThat(usuarios.size()).isEqualTo(1);
        assertThat(usuarios.get(0).getNombre()).isEqualTo("Ana García");
    }
```

En **ListaUsuariosWebTest** se ha implementado un test que comprueba que se devuelve el error correcto cuando se intenta acceder a lista de usuarios sin las credenciales necesarias:
```
@Test
    public void accesoSinLogin() throws Exception {

        this.mockMvc.perform(get("/usuarios"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("Usuario no autorizado")));

    }
```

En **UsuarioServiceTest**:

Un test para comprobar si funciona correctamente lo de bloquear usuarios:
```
@Test
    @Transactional
    public void testBloquearUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Long idUsuarioABloquear = 1L;


        // WHEN


        Usuario usuarioModificado = usuarioService.modificaEstadoUsuario(idUsuarioABloquear, true);
        Usuario usuarioBD = usuarioService.findById(idUsuarioABloquear);


        // THEN

        assertThat(usuarioModificado.getBloqueado()).isEqualTo(true);
        assertThat(usuarioBD.getBloqueado()).isEqualTo(true);

    }
```
Y otro para comprobar si deniega el acceso:
```
@Test
    @Transactional
    public void loginUsuarioBloqueado(){
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");
        usuario.setBloqueado(true);

        // WHEN

        usuarioService.registrar(usuario);

        UsuarioService.LoginStatus loginStatusUsuarioBloqueado = usuarioService.login("usuario.prueba2@gmail.com", "12345678");

        // THEN

        assertThat(loginStatusUsuarioBloqueado).isEqualTo(UsuarioService.LoginStatus.USUARIO_BLOQUEADO);
    }
```

También se ha hecho otro test que prueba el registro del usuario administrador:

```
@Test
    @Transactional
    public void servicioRegistroUsuarioAdministrador() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");
        usuario.setAdministrador(true);

        // WHEN

        usuarioService.registrar(usuario);

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());

        Boolean existeAdministrador = usuarioService.existeAdmin();
        assertThat(existeAdministrador).isEqualTo(usuario.getAdministrador());
    }
```

En **UsuarioWebTest** se ha implementado una función para comprobar el login correcto del administrador (que reedirige a `/usuarios`):

```
@Test
    public void servicioLoginUsuarioAdminOK() throws Exception {
        Usuario anaGarcia = new Usuario("ana.garcia@gmail.com");
        anaGarcia.setId(1L);
        anaGarcia.setAdministrador(true);

        when(usuarioService.login("ana.garcia@gmail.com", "12345678")).thenReturn(UsuarioService.LoginStatus.LOGIN_OK);
        when(usuarioService.findByEmail("ana.garcia@gmail.com")).thenReturn(anaGarcia);

        this.mockMvc.perform(post("/login")
                .param("eMail", "ana.garcia@gmail.com")
                .param("password", "12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));
    }
```
