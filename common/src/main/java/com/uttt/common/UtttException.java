package com.uttt.common;

@SuppressWarnings("serial")
public class UtttException extends RuntimeException {

	protected UtttException()                                        { super();               }
	protected UtttException(String message)                          { super(message);        }
	protected UtttException(Throwable cause)                         { super(cause);          }
	protected UtttException(String message, Throwable cause)         { super(message, cause); }

	// ====================================================================================================

	public static class BadBoardConfig extends UtttException {

		public BadBoardConfig()                                      { super();               }
		public BadBoardConfig(String message)                        { super(message);        }
		public BadBoardConfig(Throwable cause)                       { super(cause);          }
		public BadBoardConfig(String message, Throwable cause)       { super(message, cause); }
	}

	public static class NotBottomBoard extends UtttException {

		public NotBottomBoard()                                      { super();               }
		public NotBottomBoard(String message)                        { super(message);        }
		public NotBottomBoard(Throwable cause)                       { super(cause);          }
		public NotBottomBoard(String message, Throwable cause)       { super(message, cause); }
	}

	public static class AlreadyAtBottom extends UtttException {

		public AlreadyAtBottom()                                     { super();               }
		public AlreadyAtBottom(String message)                       { super(message);        }
		public AlreadyAtBottom(Throwable cause)                      { super(cause);          }
		public AlreadyAtBottom(String message, Throwable cause)      { super(message, cause); }
	}

	public static class NotPlayable extends UtttException {

		public NotPlayable()                                         { super();               }
		public NotPlayable(String message)                           { super(message);        }
		public NotPlayable(Throwable cause)                          { super(cause);          }
		public NotPlayable(String message, Throwable cause)          { super(message, cause); }
	}

	public static class QQQ extends UtttException {

		public QQQ()                                   { super();               }
		public QQQ(String message)                     { super(message);        }
		public QQQ(Throwable cause)                    { super(cause);          }
		public QQQ(String message, Throwable cause)    { super(message, cause); }
	}
}
