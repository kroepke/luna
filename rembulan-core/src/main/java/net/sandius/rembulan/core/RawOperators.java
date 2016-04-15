package net.sandius.rembulan.core;

import net.sandius.rembulan.util.Check;

public abstract class RawOperators {

	private RawOperators() {
		// not to be instantiated
	}

	// Arithmetic operators

//	public static long rawadd(long a, long b) {
//		return a + b;
//	}
//
//	public static double rawadd(double a, double b) {
//		return a + b;
//	}
//
//	public static long rawsub(long a, long b) {
//		return a - b;
//	}
//
//	public static double rawsub(double a, double b) {
//		return a - b;
//	}
//
//	public static long rawmul(long a, long b) {
//		return a * b;
//	}
//
//	public static double rawmul(double a, double b) {
//		return a * b;
//	}

//	public static double rawdiv(long a, long b) {
//		return ((double) a) / ((double) b);
//	}
//
//	public static double rawdiv(double a, double b) {
//		return a / b;
//	}

	public static long rawmod(long a, long b) {
		if (b == 0) throw new IllegalArgumentException("attempt to perform 'n%0'");
		else return a - b * (long) Math.floor((double) a / (double) b);
	}

	public static double rawmod(double a, double b) {
		return b != 0 ? a - b * Math.floor(a / b) : Double.NaN;
	}

	public static double rawidiv(double a, double b) {
		return Math.floor(a / b);
	}

	public static long rawidiv(long a, long b) {
		if (b == 0) throw new IllegalArgumentException("attempt to divide by zero");
		else return (long) Math.floor((double) a / (double) b);
	}

	public static double rawpow(double a, double b) {
		return Math.pow(a, b);
	}

//	public static long rawunm(long n) {
//		return -n;
//	}
//
//	public static double rawunm(double n) {
//		return -n;
//	}

	// Bitwise operators

//	public static Number rawband(long la, long lb) {
//		return la & lb;
//	}
//
//	public static Number rawbor(long la, long lb) {
//		return la | lb;
//	}
//
//	public static Number rawbxor(long la, long lb) {
//		return la ^ lb;
//	}
//
//	public static Number rawbnot(long ln) {
//		return ~ln;
//	}
//
//	public static Number rawshl(long la, long lb) {
//		return la << lb;
//	}
//
//	public static Number rawshr(long la, long lb) {
//		return la >>> lb;
//	}

	// Comparison operators

	public static boolean raweq(Object a, Object b) {
		if (a == null && b == null) {
			// two nils
			return true;
		}
		else if (a == null) {
			// b is definitely not nil; also ensures that neither a nor b is null in the tests below
			return false;
		}
		else if (a instanceof Number && b instanceof Number) {
			Number na = (Number) a;
			Number nb = (Number) b;
			// must denote the same mathematical value
			return na.doubleValue() == nb.doubleValue() && na.longValue() == nb.longValue();
		}
		else if (a instanceof Boolean || a instanceof String || a instanceof Invokable) {
			// value-based equality
			return a.equals(b);
		}
		else if (a instanceof Table
				|| a instanceof Thread
				|| a instanceof Userdata
				|| Value.isLightUserdata(a)) {

			// reference-based equality
			return a == b;
		}
		else {
			return false;
		}
	}

	public static boolean rawlt(long a, long b) {
		return a < b;
	}

	public static boolean rawlt(double a, double b) {
		return a < b;
	}

	public static boolean rawlt(String a, String b) {
		return a.compareTo(b) < 0;
	}

	public static boolean rawle(long a, long b) {
		return a <= b;
	}

	public static boolean rawle(double a, double b) {
		return a <= b;
	}

	@Deprecated
	public static boolean rawle(Number a, Number b) {
		throw new UnsupportedOperationException();  // TODO
	}

	public static boolean rawle(String a, String b) {
		return a.compareTo(b) <= 0;
	}

	// Logical operators

	public static int stringLen(String s) {
		Check.notNull(s);
		return s.getBytes().length;  // FIXME: probably wasteful!
	}

}
