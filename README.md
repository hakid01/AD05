# AD05

## Crear tablas de base datos minidrive

Para el funcionanmiento de la app se han utilizado las dos tablas que se crean con el siguiente código:

```sql
create table directorios 
(id serial primary key, 
nome text[] not null unique);

create table arquivos 
(id serial, nome text not null, 
id_directorio integer not null references directorios(id), 
arquivo bytea not null, 
primary key (nome, id_directorio));
```

## Datos de conexión db y path local
Los datos de conexión a la base de datos y el path donde se guardan los archivos en local están en un archico llamado data.json, situado dentro del proyecto en src/main/resources/data.json

Modificar en este archivo el path existente por uno válido en la máquina donde se ejecute el programa.

## Funcionamiento app minidrive
Al ejecutar se abre un frame principal con dos botones, iniciar y detener.

En la parte inferior derecha se indica el estado de la app.
En la parte inferior izquierda, si la app está corriendo se muestra el path de la carpeta raíz (útil al ejecutar dos aplicaciones con paths diferentes).

Las notificaciones se abren en un jOptionPane.Como mensaje muestra la ruta del archivo añadido. En la barra de título muestra el path al que hace referencia.
