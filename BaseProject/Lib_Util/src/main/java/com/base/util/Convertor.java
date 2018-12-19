package com.base.util;

import android.content.ContentValues;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

public class Convertor {
	private final Object obj;
	private Convertor() { this(null); }
	private Convertor(Object target) { obj = target; }
	public static final Convertor convert(Object target) {
		return new Convertor(target);
	}
	public boolean tryBoolean(boolean fallover) {
		return Convertor.tryBoolean(obj, fallover);
	}
	public boolean tryBoolean() {
		return tryBoolean(false);
	}
	public int tryInt(int fallover) {
		return Convertor.tryInt(obj, fallover);
	}
	public int tryInt() {
		return tryInt(0);
	}
	public long tryLong(long fallover) {
		return Convertor.tryLong(obj, fallover);
	}
	public long tryLong() {
		return tryLong(0);
	}
	public short tryShort(short fallover) {
		return Convertor.tryShort(obj, fallover);
	}
	public short tryShort() {
		return tryShort((short) 0);
	}
	public byte tryByte(byte fallover) {
		return Convertor.tryByte(obj, fallover);
	}
	public byte tryByte() {
		return tryByte((byte) 0);
	}
	public float tryFloat(float fallover) {
		return Convertor.tryFloat(obj, fallover);
	}
	public float tryFloat() {
		return tryFloat(0);
	}
	public double tryDouble(double fallover) {
		return Convertor.tryDouble(obj, fallover);
	}
	public double tryDouble() {
		return tryDouble(0);
	}
	public BigInteger tryBigInt(BigInteger fallover) {
		return Convertor.tryBigInt(obj, fallover);
	}
	public BigInteger tryBigInt() {
		return tryBigInt(BigInteger.ZERO);
	}
	public BigDecimal tryBigDecimal(BigDecimal fallover) {
		return Convertor.tryBigDecimal(obj, fallover);
	}
	public BigDecimal tryBigDecimal() {
		return tryBigDecimal(BigDecimal.ZERO);
	}
	public Timestamp tryTimestamp(Timestamp fallover) {
		return Convertor.tryTimestamp(obj, fallover);
	}
	public Timestamp tryTimestamp() {
		return tryTimestamp(new Timestamp(0));
	}
	public Date tryDate(Date fallover) {
		return Convertor.tryDate(obj, fallover);
	}
	public Date tryDate() {
		return tryDate(new Date(0));
	}
	public Time tryTime(Time fallover) {
		return Convertor.tryTime(obj, fallover);
	}
	public Time tryTime() {
		return tryTime(new Time(0));
	}
	public Calendar tryCalendar(DateFormat dateFormat, Calendar fallover) {
		return Convertor.tryCalendar(obj, dateFormat, fallover);
	}
	public Calendar tryCalendar(Calendar fallover) {
		return tryCalendar(SimpleDateFormat.getDateTimeInstance(), fallover);
	}
	public Calendar tryCalendar() {
		return tryCalendar(Calendar.getInstance());
	}
	public ContentValues tryContentValues() {
		return Convertor.tryContentValues(obj);
	}

	public static final boolean tryBoolean(Object target, boolean fallover) {
		try {
			return Boolean.parseBoolean(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final boolean tryBoolean(Object target) {
		return tryBoolean(target, false);
	}
	public static final int tryInt(Object target, int fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).intValue();
			} else if (target instanceof CharSequence) {
				return Integer.parseInt(target.toString());
			} else {
				return target.hashCode();
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final int tryInt(Object target) {
		return tryInt(target, 0);
	}
	public static final long tryLong(Object target, long fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).longValue();
			} else if (target instanceof CharSequence) {
				return Long.parseLong(target.toString());
			} else {
				return target.hashCode();
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final long tryLong(Object target) {
		return tryLong(target, 0);
	}
	public static final short tryShort(Object target, short fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).shortValue();
			} else if (target instanceof CharSequence) {
				return Short.parseShort(target.toString());
			} else {
				return (short) target.hashCode();
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final short tryShort(Object target) {
		return tryShort(target, (short) 0);
	}
	public static final byte tryByte(Object target, byte fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).byteValue();
			} else {
				return Byte.parseByte(target.toString());
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final byte tryByte(Object target) {
		return tryByte(target, (byte) 0);
	}
	public static final float tryFloat(Object target, float fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).floatValue();
			} else if (target instanceof CharSequence) {
				return Float.parseFloat(target.toString());
			} else {
				return target.hashCode();
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final float tryFloat(Object target) {
		return tryFloat(target, 0);
	}
	public static final double tryDouble(Object target, double fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			if (target instanceof Number) {
				return ((Number)target).doubleValue();
			} else if (target instanceof CharSequence) {
				return Double.parseDouble(target.toString());
			} else {
				return target.hashCode();
			}
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final double tryDouble(Object target) {
		return tryDouble(target, 0);
	}
	public static final BigInteger tryBigInt(Object target, BigInteger fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			return new BigInteger(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final BigInteger tryBigInt(Object target) {
		return tryBigInt(target, BigInteger.ZERO);
	}
	public static final BigDecimal tryBigDecimal(Object target, BigDecimal fallover) {
		try {
			if (target instanceof TextView) {
				target = ((TextView)target).getText();
			} else if (target instanceof View) {
				target = ((View)target).getTag();
			}
			return new BigDecimal(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final BigDecimal tryBigDecimal(Object target) {
		return tryBigDecimal(target, BigDecimal.ZERO);
	}
	public static final Timestamp tryTimestamp(Object target, Timestamp fallover) {
		if (target instanceof TextView) {
			target = ((TextView)target).getText();
		} else if (target instanceof View) {
			target = ((View)target).getTag();
		}
		if (target instanceof Calendar) 
			return new Timestamp(((Calendar)target).getTimeInMillis());
		else if (target instanceof java.util.Date) 
			return new Timestamp(((java.util.Date)target).getTime());
		else if (target instanceof Number)
			return new Timestamp(((Number)target).longValue());
		else try {
			return Timestamp.valueOf(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final Timestamp tryTimestamp(Object target) {
		return tryTimestamp(target, new Timestamp(0));
	}
	public static final Date tryDate(Object target, Date fallover) {
		if (target instanceof TextView) {
			target = ((TextView)target).getText();
		} else if (target instanceof View) {
			target = ((View)target).getTag();
		}
		if (target instanceof Calendar) 
			return new Date(((Calendar)target).getTimeInMillis());
		else if (target instanceof java.util.Date) 
			return new Date(((java.util.Date)target).getTime());
		else if (target instanceof Number)
			return new Date(((Number)target).longValue());
		else try {
			return Date.valueOf(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final Date tryDate(Object target) {
		return tryDate(target, new Date(0));
	}
	public static final Time tryTime(Object target, Time fallover) {
		if (target instanceof TextView) {
			target = ((TextView)target).getText();
		} else if (target instanceof View) {
			target = ((View)target).getTag();
		}
		if (target instanceof Calendar) 
			return new Time(((Calendar)target).getTimeInMillis());
		else if (target instanceof java.util.Date) 
			return new Time(((java.util.Date)target).getTime());
		else if (target instanceof Number)
			return new Time(((Number)target).longValue());
		else try {
			return Time.valueOf(target.toString());
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final Time tryTime(Object target) {
		return tryTime(target, new Time(0));
	}
	public static final Calendar tryCalendar(Object target, 
			DateFormat dateFormat, Calendar fallover) {
		try {
			java.util.Date d = dateFormat.parse(target.toString());
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c;
		} catch(Exception e) {
			return fallover;
		}
	}
	public static final Calendar tryCalendar(Object target, Calendar fallover) {
		return tryCalendar(target, 
				SimpleDateFormat.getDateTimeInstance(), fallover);
	}
	public static final Calendar tryCalendar(Object target) {
		return tryCalendar(target, Calendar.getInstance());
	}
	@SuppressWarnings("rawtypes")
	public static final ContentValues tryContentValues(Object target) {
		ContentValues values = new ContentValues();
		if (target instanceof ContentValues) {
			values.putAll((ContentValues) target);
		} else 
		if (target instanceof Map) {
			Map m = (Map)target;
			for(Object key : m.keySet()) {
				Object value = m.get(key);
				if (value instanceof Integer) {
					values.put(key.toString(), (Integer)value);
				} else 
				if (value instanceof Long) {
					values.put(key.toString(), (Long)value);
				}
			}
		}
		return values;
	}
	public static final ArrayList<Integer> toIntegerArrayList(Object... targets) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(Object target : targets) array.add(Convertor.tryInt(target));
		return array;
	}
	public static final ArrayList<Long> toLongArrayList(Object... targets) {
		ArrayList<Long> array = new ArrayList<Long>();
		for(Object target : targets) array.add(Convertor.tryLong(target));
		return array;
	}
	public static final ArrayList<Short> toShortArrayList(Object... targets) {
		ArrayList<Short> array = new ArrayList<Short>();
		for(Object target : targets) array.add(Convertor.tryShort(target));
		return array;
	}
	public static final ArrayList<Byte> toByteArrayList(Object... targets) {
		ArrayList<Byte> array = new ArrayList<Byte>();
		for(Object target : targets) array.add(Convertor.tryByte(target));
		return array;
	}
	public static final ArrayList<Float> toFloatArrayList(Object... targets) {
		ArrayList<Float> array = new ArrayList<Float>();
		for(Object target : targets) array.add(Convertor.tryFloat(target));
		return array;
	}
	public static final ArrayList<Double> toDoubleArrayList(Object... targets) {
		ArrayList<Double> array = new ArrayList<Double>();
		for(Object target : targets) array.add(Convertor.tryDouble(target));
		return array;
	}
	public static final ArrayList<String> toStringArrayList(Object... targets) {
		ArrayList<String> array = new ArrayList<String>();
		for(Object target : targets) array.add(target.toString());
		return array;
	}
	public static final ArrayList<CharSequence> toCharSequenceArrayList(
			Object... targets) {
		ArrayList<CharSequence> array = new ArrayList<CharSequence>();
		for(Object target : targets) array.add(target.toString());
		return array;
	}
	public static final ArrayList<Integer> toIntegerArrayList(
			int fallover, Object... targets) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(Object target : targets) 
			array.add(Convertor.tryInt(target, fallover));
		return array;
	}
	public static final ArrayList<Long> toLongArrayList(
			long fallover, Object... targets) {
		ArrayList<Long> array = new ArrayList<Long>();
		for(Object target : targets) 
			array.add(Convertor.tryLong(target, fallover));
		return array;
	}
	public static final ArrayList<Short> toShortArrayList(
			short fallover, Object... targets) {
		ArrayList<Short> array = new ArrayList<Short>();
		for(Object target : targets) 
			array.add(Convertor.tryShort(target, fallover));
		return array;
	}
	public static final ArrayList<Byte> toByteArrayList(
			byte fallover, Object... targets) {
		ArrayList<Byte> array = new ArrayList<Byte>();
		for(Object target : targets) 
			array.add(Convertor.tryByte(target, fallover));
		return array;
	}
	public static final ArrayList<Float> toFloatArrayList(
			float fallover, Object... targets) {
		ArrayList<Float> array = new ArrayList<Float>();
		for(Object target : targets) 
			array.add(Convertor.tryFloat(target, fallover));
		return array;
	}
	public static final ArrayList<Double> toDoubleArrayList(
			double fallover, Object... targets) {
		ArrayList<Double> array = new ArrayList<Double>();
		for(Object target : targets) 
			array.add(Convertor.tryDouble(target, fallover));
		return array;
	}
	public static final ArrayList<String> toStringArrayList(
			String fallover, Object... targets) {
		ArrayList<String> array = new ArrayList<String>();
		for(Object target : targets) try {
			array.add(target.toString());
		} catch (Exception e) {
			array.add(fallover);
		}
		return array;
	}
	public static final ArrayList<CharSequence> toCharSequenceArrayList(
			CharSequence fallover, Object... targets) {
		ArrayList<CharSequence> array = new ArrayList<CharSequence>();
		for(Object target : targets) try {
			array.add(target.toString());
		} catch (Exception e) {
			array.add(fallover);
		}
		return array;
	}
	public static final CharSequence[] toCharSequenceArray(Collection<?> values) {
		return toCharSequenceArray(null, values);
	}
	public static final CharSequence[] toCharSequenceArray(
			CharSequence fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		CharSequence[] array = new CharSequence[size];
		int i = 0;
		for(Object target : values) try {
			if (i>=size) break;
			array[i] = target.toString();
			i++;
		} catch (Exception e) {
			array[i] = fallover;
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final String[] toStringArray(Collection<?> values) {
		return toStringArray(null, values);
	}
	public static final String[] toStringArray(
			String fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		String[] array = new String[size];
		int i = 0;
		for(Object target : values) try {
			if (i>=size) break;
			array[i] = target.toString();
			i++;
		} catch (Exception e) {
			array[i] = fallover;
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final int[] toIntArray(Collection<?> values) {
		return toIntArray(0, values);
	}
	public static final int[] toIntArray(
			int fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		int[] array = new int[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryInt(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final long[] toLongArray(Collection<?> values) {
		return toLongArray(0, values);
	}
	public static final long[] toLongArray(
			long fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		long[] array = new long[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryLong(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final short[] toShortArray(Collection<?> values) {
		return toShortArray((short) 0, values);
	}
	public static final short[] toShortArray(
			short fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		short[] array = new short[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryShort(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final byte[] toByteArray(Collection<?> values) {
		return toByteArray((byte) 0, values);
	}
	public static final byte[] toByteArray(
			byte fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		byte[] array = new byte[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryByte(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final float[] toFloatArray(Collection<?> values) {
		return toFloatArray( 0, values);
	}
	public static final float[] toFloatArray(
			float fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		float[] array = new float[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryFloat(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final double[] toDoubleArray(Collection<?> values) {
		return toDoubleArray(0, values);
	}
	public static final double[] toDoubleArray(
			double fallover, Collection<?> values) {
		int size = values==null? 0: values.size();
		double[] array = new double[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryDouble(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final CharSequence[] toCharSequenceArray(Object... values) {
		return toCharSequenceArray(null, values);
	}
	public static final CharSequence[] toCharSequenceArray(
			CharSequence fallover, Object... values) {
		int size = values==null? 0: values.length;
		CharSequence[] array = new CharSequence[size];
		int i = 0;
		for(Object target : values) try {
			if (i>=size) break;
			array[i] = target.toString();
			i++;
		} catch (Exception e) {
			array[i] = fallover;
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final String[] toStringArray(Object... values) {
		return toStringArray(null, values);
	}
	public static final String[] toStringArray(
			String fallover, Object... values) {
		int size = values==null? 0: values.length;
		String[] array = new String[size];
		int i = 0;
		for(Object target : values) try {
			if (i>=size) break;
			array[i] = target.toString();
			i++;
		} catch (Exception e) {
			array[i] = fallover;
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final int[] toIntArray(Object... values) {
		return toIntArray(0, values);
	}
	public static final int[] toIntArray(
			int fallover, Object... values) {
		int size = values==null? 0: values.length;
		int[] array = new int[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryInt(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final long[] toLongArray(Object... values) {
		return toLongArray(0, values);
	}
	public static final long[] toLongArray(
			long fallover, Object... values) {
		int size = values==null? 0: values.length;
		long[] array = new long[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryLong(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final short[] toShortArray(Object... values) {
		return toShortArray((short) 0, values);
	}
	public static final short[] toShortArray(
			short fallover, Object... values) {
		int size = values==null? 0: values.length;
		short[] array = new short[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryShort(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final byte[] toByteArray(Object... values) {
		return toByteArray((byte) 0, values);
	}
	public static final byte[] toByteArray(
			byte fallover, Object... values) {
		int size = values==null? 0: values.length;
		byte[] array = new byte[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryByte(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final float[] toFloatArray(Object... values) {
		return toFloatArray( 0, values);
	}
	public static final float[] toFloatArray(
			float fallover, Object... values) {
		int size = values==null? 0: values.length;
		float[] array = new float[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryFloat(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
	public static final double[] toDoubleArray(Object... values) {
		return toDoubleArray(0, values);
	}
	public static final double[] toDoubleArray(
			double fallover, Object... values) {
		int size = values==null? 0: values.length;
		double[] array = new double[size];
		int i = 0;
		for(Object target : values) {
			if (i>=size) break;
			array[i] = Convertor.tryDouble(target, fallover);
			i++;
		}
		for(; i<size; i++) array[i] = fallover;
		return array;
	}
}
