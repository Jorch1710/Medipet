package com.example.medipet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medipet.db";
    private static final int DATABASE_VERSION = 2; // Aumentado para forzar onUpgrade

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de mascotas
        db.execSQL("CREATE TABLE mascotas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "peso TEXT," +
                "raza TEXT," +
                "fecha TEXT," +
                "sexo TEXT," +
                "tamanio TEXT," +
                "imagen BLOB," +
                "usuario_nombre TEXT" +
                ");");

        // Tabla de usuarios
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "telefono TEXT," +
                "correo TEXT," +
                "contrasena TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mascotas");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    // Insertar usuario nuevo
    public boolean insertarUsuario(String nombre, String telefono, String correo, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("telefono", telefono);
        values.put("correo", correo);
        values.put("contrasena", contrasena);
        db.insert("usuarios", null, values);
        db.close();
        return true;
    }

    // Verificar si el usuario y contraseña son válidos
    public boolean verificarUsuario(String nombre, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?", new String[]{nombre, contrasena});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    // Insertar mascota
    public void insertarMascota(String nombre, String peso, String raza, String fecha, String sexo, String tamanio, byte[] imagen, String usuarioNombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("peso", peso);
        values.put("raza", raza);
        values.put("fecha", fecha);
        values.put("sexo", sexo);
        values.put("tamanio", tamanio);
        values.put("imagen", imagen);
        values.put("usuario_nombre", usuarioNombre);
        db.insert("mascotas", null, values);
        db.close();
    }

    // Obtener mascotas por usuario
    // Cambia este método en DBHelper.java
    public List<Mascota> obtenerMascotasPorUsuario(String usuarioNombre) {
        List<Mascota> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM mascotas WHERE usuario_nombre = ?", new String[]{usuarioNombre});

        if (cursor.moveToFirst()) {
            do {
                Mascota mascota = new Mascota();
                mascota.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                mascota.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                mascota.setPeso(cursor.getString(cursor.getColumnIndexOrThrow("peso")));
                mascota.setRaza(cursor.getString(cursor.getColumnIndexOrThrow("raza")));
                mascota.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));
                mascota.setSexo(cursor.getString(cursor.getColumnIndexOrThrow("sexo")));
                mascota.setTamanio(cursor.getString(cursor.getColumnIndexOrThrow("tamanio")));
                mascota.setImagen(cursor.getBlob(cursor.getColumnIndexOrThrow("imagen")));
                lista.add(mascota);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }



    // Eliminar mascota
    public void eliminarMascota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("mascotas", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
