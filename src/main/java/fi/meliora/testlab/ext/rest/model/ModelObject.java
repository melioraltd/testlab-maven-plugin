package fi.meliora.testlab.ext.rest.model;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Marko Kanala
 */
public class ModelObject implements Serializable {
    private static final int TOSTRING_CUT = "fi.meliora.testlab.ext.rest.model.".length();
    private static final boolean SKIP_LAZY_FIELDS = true;

    protected int toStringCut() {
        return TOSTRING_CUT;
    }

    /**
     * Common reflected toString()-method to print out transfer object information in XML format.
     *
     * @param indent indent amount
     * @param written contains object already written to prevent deadlock
     * @return object as String
     */
    public String toString(int indent, Set<ModelObject> written) {
        StringBuilder buf = new StringBuilder();
        Class clazz = this.getClass();
        Method[] methods = clazz.getMethods();
        buf.append('\n');
        Method m;
        String name;
        java.lang.Object o;
        Iterator list = null;
        for (Method method : methods) {
            m = method;
            name = m.getName();
            if ((name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2)) {
                if (name.startsWith("is")) {
                    name = name.substring(2);
                } else {
                    name = name.substring(3);
                }
                for (int j = 0; j < indent; j++)
                    buf.append(' ');
                buf.append("<");
                buf.append(name);
                buf.append(">");
                try {
                    boolean skip = false;
                    if (SKIP_LAZY_FIELDS) {
                        try {
                            Field f = clazz.getDeclaredField(Character.toLowerCase(name.charAt(0)) + name.substring(1));
                            for (Annotation a : f.getAnnotations()) {
                                String anno = a.toString();
                                if (anno.contains("fetch=LAZY")) {
                                    skip = true;
                                    break;
                                }
                            }
                        } catch (NoSuchFieldException nsfe) {
                        }
                    }
                    if (skip) {
                        buf.append("[skipped lazy loaded field]");
                    } else {
                        o = m.invoke(this, (Object[]) null);
                        if (o == this) {
                            buf.append("[super]");
                        } else if (o instanceof ModelObject) {
                            if (written.contains(o)) {
                                buf.append("[parent ");
                                buf.append(name);
                                buf.append("]");
                            } else {
                                buf.append(((ModelObject) o).toString(indent + 4, written));
                                for (int j = 0; j < indent; j++)
                                    buf.append(' ');
                            }
                        } else if (o instanceof Collection) {
//                        if(SKIP_LAZY_FIELDS && o.getClass().getName().startsWith("org.eclipse.persistence.indirection")) {
//                            buf.append("[lazy collection]");
//                        } else {
                            buf.append('\n');
                            list = ((Collection) o).iterator();
                            ModelObject xe;
                            while (list.hasNext()) {
                                Object obi = list.next();
                                if (obi instanceof ModelObject) {
                                    xe = (ModelObject) obi;
                                    for (int j = 0; j < indent + 4; j++)
                                        buf.append(' ');
                                    buf.append("<");
                                    buf.append(xe.getClass().getName().substring(toStringCut()));
                                    buf.append(">");
                                    buf.append(xe.toString(indent + 8, written));
                                    for (int j = 0; j < indent + 4; j++)
                                        buf.append(' ');
                                    buf.append("</");
                                    buf.append(xe.getClass().getName().substring(toStringCut()));
                                    buf.append(">");
                                    buf.append('\n');
                                } else {
                                    for (int j = 0; j < indent; j++)
                                        buf.append(' ');
                                    buf.append(obi);
                                    buf.append('\n');
                                }
                            }
                            for (int j = 0; j < indent; j++)
                                buf.append(' ');
//                        }
                        } else {
                            buf.append(o);
                        }
                    }
                } catch (Exception iae) {
                    buf.append("[call to getter failed]");
                }
                buf.append("</");
                buf.append(name);
                buf.append(">\n");
                written.add(this);
            }
        }
        return buf.toString();
    }

    /**
     * Common reflected toString()-method to print out transfer object information in XML format.
     *
     * @return object as String
     */
    public String toString() {
        StringBuilder buf = new StringBuilder(256);
        buf.append("\n<");
        buf.append(getClass().getName().substring(toStringCut()));
        buf.append(">");
        buf.append(toString(4, new HashSet<ModelObject>()));
        buf.append("</");
        buf.append(getClass().getName().substring(toStringCut()));
        buf.append(">\n");
        return buf.toString();
    }

}
