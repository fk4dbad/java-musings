import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Triangle {
  public static class Vertex {}
  
  private Vertex a, b, c;

  public Set<Vertex> getVertices() {
    return new HashSet<>(Arrays.asList(a, b, c));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Triangle other = (Triangle) obj;
    return getVertices().equals(other.getVertices());
  }
  
  
}
