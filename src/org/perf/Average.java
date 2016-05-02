package org.perf;

import org.jgroups.util.Bits;
import org.jgroups.util.Streamable;
import org.jgroups.util.Util;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Ported from JGroups master - remove when moving to JGroups 3.6.10
 * @author Bela Ban
 */
public class Average implements Streamable {
    protected double avg;
    protected long   count;


    public <T extends Average> T add(long num) {
        // If the product of the average and the number of samples would be greater than Long.MAX_VALUE, we have
        // to reset the count and average to prevent a long overflow. This will temporarily lose the sample history, and
        // the next sample will be the new average, but with more data points, the average should become more precise.
        // Note that overflow should be extremely seldom, as we usually use Average in cases where we don't have a huge
        // number of sample and the average is pretty small (e.g. an RPC invocation)
        if(Util.productGreaterThan(count, (long)Math.ceil(avg), Long.MAX_VALUE))
            clear();
        double total=count * avg;
        avg=(total + num) / ++count;
        return (T)this;
    }

    /** Merges this average with another one */
    public <T extends Average> T merge(T other) {
        if(Util.productGreaterThan(count, (long)Math.ceil(avg), Long.MAX_VALUE) ||
          Util.productGreaterThan(other.count(), (long)Math.ceil(other.average()), Long.MAX_VALUE)) {
            // the above computation is not correct as the sum of the 2 products can still lead to overflow
            // a non-weighted avg
            avg=avg + other.average() / 2.0;
        }
        else { // compute the new average weighted by count
            long total_count=count + other.count();
            avg=(count * avg + other.count() * other.average()) / total_count;
            count=total_count/2;
        }
        return (T)this;
    }


    public double getAverage() {return avg;}
    public double average()    {return avg;}
    public long   getCount()   {return count;}
    public long   count()      {return count;}

    public void clear() {
        avg=0.0;
        count=0;
    }

    public String toString() {
        return String.valueOf(getAverage());
    }

    public void writeTo(DataOutput out) throws Exception {
        out.writeDouble(avg);
        Bits.writeLong(count, out);
    }

    public void readFrom(DataInput in) throws Exception {
        avg=in.readDouble();
        count=Bits.readLong(in);
    }
}
