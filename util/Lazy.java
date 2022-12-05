package cs2030.util;

import java.util.Optional;
import java.util.function.Supplier;

public class Lazy<T> {
  private final Supplier<T> supplier;
  private Optional<T> value;


  public Lazy(Supplier<T> supplier) {
    this.supplier = supplier;
    this.value = Optional.empty();
  }

  public static <T> Lazy<T> of(Supplier<T> supplier) {
    return new Lazy<T>(supplier);
  }

  public T get() {
    T t = value.orElseGet(supplier);
    this.value = Optional.of(t);
    return t;
  }
}
