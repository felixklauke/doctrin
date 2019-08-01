package de.d3adspace.doctrin.commons.message;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface DoctrinMessageContext {

  /**
   * Resume the execution flow with the given message.
   *
   * @param message The message.
   */
  void resume(DoctrinMessage message);
}
