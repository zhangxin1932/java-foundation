package com.zy.foundation.httpclient.ssl;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 根据传递的SSLOption构造SSL上下文。请参考JSSE获取相关API的层次参考。
 *
 */
public final class SSLManager {
  private SSLManager() {

  }

  public static SSLContext createSSLContext(SSLOption option, SSLCustom custom) {
    try {
      String keyStoreName = custom.getFullPath(option.getKeyStore());
      KeyManager[] keymanager;
      if (keyStoreName != null && new File(keyStoreName).exists()) {
        char[] keyStoreValue =
            custom.decode(option.getKeyStoreValue().toCharArray());
        KeyStore keyStore =
            KeyStoreUtil.createKeyStore(keyStoreName,
                option.getKeyStoreType(),
                keyStoreValue);
        keymanager =
            KeyStoreUtil.createKeyManagers(keyStore, keyStoreValue);
      } else {
        keymanager = null;
      }

      String trustStoreName = custom.getFullPath(option.getTrustStore());
      TrustManager[] trustManager;
      if (trustStoreName != null && new File(trustStoreName).exists()) {
        char[] trustStoreValue =
            custom.decode(option.getTrustStoreValue().toCharArray());
        KeyStore trustStore =
            KeyStoreUtil.createKeyStore(trustStoreName,
                option.getTrustStoreType(),
                trustStoreValue);
        trustManager =
            KeyStoreUtil.createTrustManagers(trustStore);
      } else {
        trustManager = new TrustManager[] {new TrustAllManager()};
      }

      TrustManager[] wrapped = new TrustManager[trustManager.length];
      for (int i = 0; i < trustManager.length; i++) {
        wrapped[i] =
            new TrustManagerExt((X509ExtendedTrustManager) trustManager[i],
                option, custom);
      }

      // ?: ssl context version
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(keymanager, wrapped, new SecureRandom());
      return context;
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("NoSuchAlgorithmException."
          + e.getMessage());
    } catch (KeyManagementException e) {
      throw new IllegalArgumentException("KeyManagementException."
          + e.getMessage());
    }
  }

  public static SSLSocketFactory createSSLSocketFactory(SSLOption option, SSLCustom custom) {
    SSLContext context = createSSLContext(option, custom);
    SSLSocketFactory factory = context.getSocketFactory();
    String[] supported = factory.getSupportedCipherSuites();
    String[] eanbled = option.getCiphers().split(",");
    return new SSLSocketFactoryExt(factory, getEnabledCiphers(supported, eanbled),
        option.getProtocols().split(","));
  }

  public static SSLEngine createSSLEngine(SSLOption option, SSLCustom custom) {
    SSLContext context = createSSLContext(option, custom);
    SSLEngine engine =
        context.createSSLEngine();
    engine.setEnabledProtocols(option.getProtocols().split(","));
    String[] supported = engine.getSupportedCipherSuites();
    String[] eanbled = option.getCiphers().split(",");
    engine.setEnabledCipherSuites(getEnabledCiphers(supported, eanbled));
    engine.setNeedClientAuth(option.isAuthPeer());
    return engine;
  }

  public static SSLEngine createSSLEngine(SSLOption option, SSLCustom custom, String peerHost, int peerPort) {
    SSLContext context = createSSLContext(option, custom);
    SSLEngine engine =
        context.createSSLEngine(peerHost, peerPort);
    engine.setEnabledProtocols(option.getProtocols().split(","));
    String[] supported = engine.getSupportedCipherSuites();
    String[] eanbled = option.getCiphers().split(",");
    engine.setEnabledCipherSuites(getEnabledCiphers(supported, eanbled));
    engine.setNeedClientAuth(option.isAuthPeer());
    return engine;
  }

  public static SSLServerSocket createSSLServerSocket(SSLOption option,
      SSLCustom custom) {
    try {
      SSLContext context = createSSLContext(option, custom);
      SSLServerSocketFactory factory = context.getServerSocketFactory();
      SSLServerSocket socket =
          (SSLServerSocket) factory.createServerSocket();
      socket.setEnabledProtocols(option.getProtocols().split(","));
      String[] supported = socket.getSupportedCipherSuites();
      String[] eanbled = option.getCiphers().split(",");
      socket.setEnabledCipherSuites(getEnabledCiphers(supported, eanbled));
      socket.setNeedClientAuth(option.isAuthPeer());
      return socket;
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("unkown host");
    } catch (IOException e) {
      throw new IllegalArgumentException("unable create socket");
    }
  }

  public static SSLSocket createSSLSocket(SSLOption option, SSLCustom custom) {
    try {
      SSLContext context = createSSLContext(option, custom);
      SSLSocketFactory facroty = context.getSocketFactory();
      SSLSocket socket =
          (SSLSocket) facroty.createSocket();
      socket.setEnabledProtocols(option.getProtocols().split(","));
      String[] supported = socket.getSupportedCipherSuites();
      String[] enabled = option.getCiphers().split(",");
      socket.setEnabledCipherSuites(getEnabledCiphers(supported, enabled));
      return socket;
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("unkown host");
    } catch (IOException e) {
      throw new IllegalArgumentException("unable create socket");
    }
  }

  private static String[] getEnabledCiphers(String[] supported,
      String[] enabled) {
    String[] result = new String[enabled.length];
    int count = 0;
    for (String e : enabled) {
      for (String s : supported) {
        if (e.equals(s)) {
          result[count++] = e;
          break;
        }
      }
    }

    if (count == 0) {
      throw new IllegalArgumentException("no enabled cipher suits.");
    }

    String[] r = new String[count];
    System.arraycopy(result, 0, r, 0, count);
    return r;
  }

  public static String[] getEnabledCiphers(String enabledCiphers) {
    SSLOption option = new SSLOption();
    option.setProtocols("TLSv1.2");
    option.setCiphers(enabledCiphers);
    SSLCustom custom = SSLCustom.defaultSSLCustom();
    SSLSocket socket = createSSLSocket(option, custom);
    return socket.getEnabledCipherSuites();
  }
}
