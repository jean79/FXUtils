package net.fxutils.common;

@FunctionalInterface
public interface Function1<R, O> {

    R to(O r);

}
