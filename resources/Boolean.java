public class Boolean {
  boolean and(boolean[] arr) {
    for (int i = 0; i < arr.length; ++i) {
      if (!arr[i]) return false;
    }
    return true;
  }

  boolean and2(boolean[] arr) {
    for (boolean b : arr) {
      if (!b) return false;
    }
    return true;
  }

  boolean or(boolean[] arr) {
    for (int i = 0; i < arr.length; ++i) {
      if (arr[i]) return true;
    }
    return false;
  }

}
