package com.thedevjade.cire.resourcepacks.httpd;

/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

import org.bukkit.Bukkit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copied over from https://resources.oreilly.com/examples/9780596002855/blob/master/CD-ROM/examples/ch12/TinyHttpd.java
 * A mini http daemon intended to serve client resource pack requests.
 *
 * @author BananaPuncher714
 */
public class MineHttpd extends Thread {
  protected final int port;
  protected final ServerSocket socket;
  private volatile boolean running = true;

  public MineHttpd(int port) throws IOException {
    this.port = port;
    socket = new ServerSocket(port);
    socket.setReuseAddress(true);
  }

  @Override
  public void run() {
    while (running) {
      try {
        new Thread(new MineConnection(this, socket.accept())).start();
      } catch (IOException e) {
        Bukkit.getLogger().warning("A thread was interrupted in a mini http daemon!");
      }
    }
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void terminate() {
    running = false;
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Executes whenever a client requests something
   *
   * @param connection The connection that is requesting a file
   * @param request    The path to the file requested
   * @return A file to return to them; null if the file they requested is invalid
   */
  public File requestFileCallback(MineConnection connection, String request) {
    return null;
  }

  /**
   * Occurs once the file has been sent
   *
   * @param connection The connection that successfully got sent a file
   * @param request    The request of the client
   */
  public void onSuccessfulRequest(MineConnection connection, String request) {
  }

  /**
   * Called before the requestFileCallback; Contains the original request
   *
   * @param connection The connection that has sent a request
   * @param request    The raw request of the client
   */
  public void onClientRequest(MineConnection connection, String request) {
  }

  /**
   * Handle http error response codes here
   *
   * @param connection The connection that is getting an error on request
   * @param code       The http response code
   */
  public void onRequestError(MineConnection connection, int code) {
  }

  public class MineConnection implements Runnable {
    protected final MineHttpd server;
    protected final Socket client;

    public MineConnection(MineHttpd server, Socket client) {
      this.server = server;
      this.client = client;
    }

    public Socket getClient() {
      return client;
    }

    @Override
    public void run() {
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.ISO_8859_1));
        OutputStream out = client.getOutputStream();
        PrintWriter pout = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.ISO_8859_1), true);
        String request = in.readLine();
        onClientRequest(this, request);

        Matcher get = Pattern.compile("GET /?(\\S*).*").matcher(request);
        if (get.matches()) {
          request = get.group(1);
          File result = requestFileCallback(this, request);
          if (result == null) {
            pout.println("HTTP/1.0 400 Bad Request");
            onRequestError(this, 400);
          } else {
            try {
              // Writes zip files specifically; Designed for resource pack hosting
              out.write("HTTP/1.0 200 OK\r\n".getBytes());
              out.write("Content-Type: application/zip\r\n".getBytes());
              out.write(("Content-Length: " + result.length() + "\r\n").getBytes());
              out.write(("Date: " + new Date().toGMTString() + "\r\n").getBytes());
              out.write("Server: MineHttpd\r\n\r\n".getBytes());
              FileInputStream fis = new FileInputStream(result);
              byte[] data = new byte[64 * 1024];
              for (int read; (read = fis.read(data)) > -1; ) {
                out.write(data, 0, read);
              }
              out.flush();
              fis.close();
              onSuccessfulRequest(this, request);
            } catch (FileNotFoundException e) {
              pout.println("HTTP/1.0 404 Object Not Found");
              onRequestError(this, 404);
            }
          }
        } else {
          pout.println("HTTP/1.0 400 Bad Request");
          onRequestError(this, 400);
        }
        client.close();
      } catch (IOException e) {
        System.out.println("I/O error " + e);
      }
    }
  }
}
