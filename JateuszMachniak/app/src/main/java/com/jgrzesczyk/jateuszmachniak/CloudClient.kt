package com.jgrzesczyk.jateuszmachniak

import com.thegrizzlylabs.sardineandroid.DavResource
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import java.io.InputStream

object CloudClient {
    @JvmStatic
    private var sardine = OkHttpSardine()
    private var ENDPOINT = "http://localhost/remote.php/webdav/"

    /**
     * Logowanie do clouda
     */
    fun login(username: String, password: String, address: String): CloudClient {
        sardine.setCredentials(username, password)
        ENDPOINT = "http://$address/remote.php/webdav/"
        return this
    }

    /**
     * Zwraca true dla poprawnych credentials otherwise false
     */
    fun validCredentials(): Boolean {
        return try {
            getList("")
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Pobieranie Plikow
     */
    fun getFile(path: String, fileName: String): InputStream {
        return sardine.get(ENDPOINT + path + fileName)
    }

    /**
     * Upload plikow
     */
    fun putFile(path: String, fileName: String, data: ByteArray) {
        sardine.put(ENDPOINT + path + fileName, data)
    }

    /**
     * Usuwamy tak samo katalogi jak i pliki
     */
    fun deleteResource(path: String, resourceName: String) {
        sardine.delete(ENDPOINT + path + resourceName)
    }

    /**
     * Dostajemy liste(Objektow DavResource) plikow i katalogow w aktualnej sciezce,wazne jest to, ze pierwszy rekord jest to nazwa katalogu w ktorym jestesmy
     * defaultowo(pusty path) dostajemy folder webdav czyli taki root.
     * Objekty DavResource maja potrzebne nam metody: name, isDirectory ... jak i wiele innych, jak cos pytac mnie -> czyszczonik
     */
    fun getList(path: String): MutableList<DavResource>? {
        return sardine.list(ENDPOINT + path)
    }

    fun createDirectory(path: String, dirName: String) {
        sardine.createDirectory("$ENDPOINT$path/$dirName")
    }

    /**
     *  Tworzenie nowej sciezki przy wchodzeniu do folderu
     *  Defaultowo jestesmy w "" to nasz root
     */
    fun forwardPath(path: String, dirName: String): String {
        return "$path$dirName/"
    }

    /**
     *  Tworzenie nowej sciezki przy cofaniu sie w drzewie folderow
     *  Odporne na wracanie z Roota do roota
     */
    fun backwardPath(path: String): String {
        if (path.isNotEmpty()) {
            val newPath = path.substring(0, path.lastIndexOf('/'))
            return newPath.substring(0, newPath.lastIndexOf('/') + 1)
        }
        return ""
    }
}
