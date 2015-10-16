package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.*;

public class Agent {

  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        // En caso de que se necesite modificar la clase que se desea intervenir se agregar cambia aqui pero a modo de que fuesen carpetas
        if ("org/springframework/web/servlet/DispatcherServlet".equals(s)) {
          try {
            ClassPool cp = ClassPool.getDefault();
            // Se carga el class loader del web
            cp.insertClassPath(new LoaderClassPath(classLoader));
            // Se carga la clase necesaria con el qualified name
            CtClass cc = cp.get("org.springframework.web.servlet.DispatcherServlet");
            // Se interviene el metodo que se requiere
            CtMethod m = cc.getDeclaredMethod("doDispatch");
            System.out.println("method : " + m);
            m.insertBefore("{ java.util.Enumeration params = $1.getParameterNames(); while(params.hasMoreElements()) { String paramName = (String) params.nextElement(); System.out.println(\"<<<<<<<>>>>>>> \" + paramName + \" : \" + $1.getParameter(paramName)); } }");
            byte[] byteCode = cc.toBytecode();
            cc.detach();
            return byteCode;
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }

        return null;
      }
    });
  }

}

