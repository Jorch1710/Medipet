package com.example.medipet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "medipet.db";
    private static final int DATABASE_VERSION = 3; // Incrementa si cambias el esquema

    // Tabla usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USUARIO_ID_PK = "id";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_TELEFONO = "telefono";
    public static final String COLUMN_USUARIO_CORREO = "correo";
    public static final String COLUMN_USUARIO_CONTRASENA = "contrasena";

    // Tabla mascotas
    public static final String TABLE_MASCOTAS = "mascotas";
    public static final String COLUMN_MASCOTA_ID_PK = "id";
    public static final String COLUMN_MASCOTA_NOMBRE = "nombre";
    public static final String COLUMN_MASCOTA_PESO = "peso";
    public static final String COLUMN_MASCOTA_RAZA = "raza";
    public static final String COLUMN_MASCOTA_FECHA = "fecha";
    public static final String COLUMN_MASCOTA_SEXO = "sexo";
    public static final String COLUMN_MASCOTA_TAMANIO = "tamanio";
    public static final String COLUMN_MASCOTA_IMAGEN = "imagen";
    public static final String COLUMN_MASCOTA_USUARIO_NOMBRE_FK = "usuario_nombre";
    // Considera una columna COLUMN_MASCOTA_USUARIO_ID_FK INTEGER para FK real a TABLE_USUARIOS(id)

    // Tabla citas
    public static final String TABLE_CITAS = "citas";
    public static final String COLUMN_CITA_ID_PK = "id";
    public static final String COLUMN_CITA_USUARIO_ID_FK = "usuario_id";
    public static final String COLUMN_CITA_NOMBRE_SUCURSAL = "nombre_sucursal";
    public static final String COLUMN_CITA_MOTIVO = "motivo_cita";
    public static final String COLUMN_CITA_DIRECCION = "direccion_cita";
    public static final String COLUMN_CITA_FECHA = "fecha_cita";
    public static final String COLUMN_CITA_HORA = "hora_cita";
    public static final String COLUMN_CITA_IMAGEN_SUCURSAL = "imagen_sucursal";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (" +
                COLUMN_USUARIO_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USUARIO_NOMBRE + " TEXT UNIQUE," +
                COLUMN_USUARIO_TELEFONO + " TEXT," +
                COLUMN_USUARIO_CORREO + " TEXT UNIQUE," +
                COLUMN_USUARIO_CONTRASENA + " TEXT" +
                ");";
        db.execSQL(CREATE_TABLE_USUARIOS);

        String CREATE_TABLE_MASCOTAS = "CREATE TABLE " + TABLE_MASCOTAS + " (" +
                COLUMN_MASCOTA_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MASCOTA_NOMBRE + " TEXT," +
                COLUMN_MASCOTA_PESO + " TEXT," +
                COLUMN_MASCOTA_RAZA + " TEXT," +
                COLUMN_MASCOTA_FECHA + " TEXT," +
                COLUMN_MASCOTA_SEXO + " TEXT," +
                COLUMN_MASCOTA_TAMANIO + " TEXT," +
                COLUMN_MASCOTA_IMAGEN + " BLOB," +
                COLUMN_MASCOTA_USUARIO_NOMBRE_FK + " TEXT" +
                ");";
        db.execSQL(CREATE_TABLE_MASCOTAS);

        String CREATE_TABLE_CITAS = "CREATE TABLE " + TABLE_CITAS + " (" +
                COLUMN_CITA_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITA_USUARIO_ID_FK + " INTEGER NOT NULL," +
                COLUMN_CITA_NOMBRE_SUCURSAL + " TEXT," +
                COLUMN_CITA_MOTIVO + " TEXT," +
                COLUMN_CITA_DIRECCION + " TEXT," +
                COLUMN_CITA_FECHA + " TEXT," +
                COLUMN_CITA_HORA + " TEXT," +
                COLUMN_CITA_IMAGEN_SUCURSAL + " BLOB," +
                "FOREIGN KEY(" + COLUMN_CITA_USUARIO_ID_FK + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID_PK + ")" +
                " ON DELETE CASCADE" +
                ");";
        db.execSQL(CREATE_TABLE_CITAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASCOTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    public boolean insertarUsuario(String nombre, String telefono, String correo, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_NOMBRE, nombre);
        values.put(COLUMN_USUARIO_TELEFONO, telefono);
        values.put(COLUMN_USUARIO_CORREO, correo);
        values.put(COLUMN_USUARIO_CONTRASENA, contrasena);

        try {
            long result = db.insertOrThrow(TABLE_USUARIOS, null, values);
            return result != -1;
        } catch (android.database.SQLException e) {
            Log.e(TAG, "insertarUsuario: Falla al insertar usuario " + nombre + ". " + e.getMessage());
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public boolean verificarUsuario(String nombre, String contrasena) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean existe = false;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_USUARIOS,
                    new String[]{COLUMN_USUARIO_ID_PK},
                    COLUMN_USUARIO_NOMBRE + " = ? AND " + COLUMN_USUARIO_CONTRASENA + " = ?",
                    new String[]{nombre, contrasena},
                    null, null, null, "1");
            existe = (cursor != null && cursor.moveToFirst());
        } catch (Exception e) {
            Log.e(TAG, "Error en verificarUsuario para " + nombre, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return existe;
    }

    public int obtenerIdUsuarioPorCredenciales(String nombre, String contrasena) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int usuarioId = -1;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    TABLE_USUARIOS,
                    new String[]{COLUMN_USUARIO_ID_PK},
                    COLUMN_USUARIO_NOMBRE + " = ? AND " + COLUMN_USUARIO_CONTRASENA + " = ?",
                    new String[]{nombre, contrasena},
                    null, null, null, "1"
            );
            if (cursor != null && cursor.moveToFirst()) {
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_ID_PK));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en obtenerIdUsuarioPorCredenciales para " + nombre, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return usuarioId;
    }

    public int obtenerIdUsuarioPorNombre(String nombre) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int usuarioId = -1;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    TABLE_USUARIOS,
                    new String[]{COLUMN_USUARIO_ID_PK},
                    COLUMN_USUARIO_NOMBRE + " = ?",
                    new String[]{nombre},
                    null, null, null, "1"
            );
            if (cursor != null && cursor.moveToFirst()) {
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_ID_PK));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en obtenerIdUsuarioPorNombre para " + nombre, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return usuarioId;
    }

    public boolean insertarMascota(String nombre, String peso, String raza, String fecha, String sexo, String tamanio, byte[] imagen, String usuarioNombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MASCOTA_NOMBRE, nombre);
        values.put(COLUMN_MASCOTA_PESO, peso);
        values.put(COLUMN_MASCOTA_RAZA, raza);
        values.put(COLUMN_MASCOTA_FECHA, fecha);
        values.put(COLUMN_MASCOTA_SEXO, sexo);
        values.put(COLUMN_MASCOTA_TAMANIO, tamanio);
        values.put(COLUMN_MASCOTA_IMAGEN, imagen);
        values.put(COLUMN_MASCOTA_USUARIO_NOMBRE_FK, usuarioNombre);

        try {
            long result = db.insertOrThrow(TABLE_MASCOTAS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar mascota " + nombre, e);
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Mascota> obtenerMascotasPorUsuario(String usuarioNombre) {
        List<Mascota> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_MASCOTAS,
                    null,
                    COLUMN_MASCOTA_USUARIO_NOMBRE_FK + " = ?",
                    new String[]{usuarioNombre},
                    null, null, COLUMN_MASCOTA_NOMBRE + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Mascota mascota = new Mascota();
                    mascota.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_ID_PK)));
                    mascota.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_NOMBRE)));
                    mascota.setPeso(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_PESO)));
                    mascota.setRaza(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_RAZA)));
                    mascota.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_FECHA)));
                    mascota.setSexo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_SEXO)));
                    mascota.setTamanio(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_TAMANIO)));
                    mascota.setImagen(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_MASCOTA_IMAGEN)));
                    lista.add(mascota);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en obtenerMascotasPorUsuario para " + usuarioNombre, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return lista;
    }

    public boolean eliminarMascota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        try {
            count = db.delete(TABLE_MASCOTAS, COLUMN_MASCOTA_ID_PK + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Error en eliminarMascota con ID: " + id, e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return count > 0;
    }

    public boolean insertarCita(int usuarioId, String nombreSucursal, String motivoCita, String direccionCita, String fechaCita, String horaCita, byte[] imagenSucursal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITA_USUARIO_ID_FK, usuarioId);
        values.put(COLUMN_CITA_NOMBRE_SUCURSAL, nombreSucursal);
        values.put(COLUMN_CITA_MOTIVO, motivoCita);
        values.put(COLUMN_CITA_DIRECCION, direccionCita);
        values.put(COLUMN_CITA_FECHA, fechaCita);
        values.put(COLUMN_CITA_HORA, horaCita);
        if (imagenSucursal != null && imagenSucursal.length > 0) {
            values.put(COLUMN_CITA_IMAGEN_SUCURSAL, imagenSucursal);
        } else {
            values.putNull(COLUMN_CITA_IMAGEN_SUCURSAL);
        }

        try {
            long result = db.insertOrThrow(TABLE_CITAS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar cita para usuario ID: " + usuarioId, e);
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<CitaModel> obtenerCitasPorUsuario(int usuarioId) {
        List<CitaModel> listaCitas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    TABLE_CITAS,
                    null,
                    COLUMN_CITA_USUARIO_ID_FK + " = ?",
                    new String[]{String.valueOf(usuarioId)},
                    null, null,
                    COLUMN_CITA_FECHA + " DESC, " + COLUMN_CITA_HORA + " DESC"
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CitaModel cita = new CitaModel();
                    cita.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CITA_ID_PK)));
                    cita.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CITA_USUARIO_ID_FK)));
                    cita.setNombreCitaSucursal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITA_NOMBRE_SUCURSAL)));
                    cita.setMotivoCita(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITA_MOTIVO)));
                    cita.setDireccionSucursal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITA_DIRECCION)));
                    cita.setFechaCita(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITA_FECHA)));
                    cita.setHoraCita(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITA_HORA)));

                    int imagenColIndex = cursor.getColumnIndexOrThrow(COLUMN_CITA_IMAGEN_SUCURSAL);
                    if (!cursor.isNull(imagenColIndex)) {
                        cita.setImagenCitaSucursal(cursor.getBlob(imagenColIndex));
                    } else {
                        cita.setImagenCitaSucursal(null);
                    }
                    listaCitas.add(cita);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error general al obtener citas por usuario ID " + usuarioId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return listaCitas;
    }

    public boolean eliminarCita(int citaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        try {
            count = db.delete(TABLE_CITAS, COLUMN_CITA_ID_PK + " = ?", new String[]{String.valueOf(citaId)});
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar cita con ID: " + citaId, e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return count > 0;
    }
}