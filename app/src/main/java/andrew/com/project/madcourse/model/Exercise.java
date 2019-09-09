package andrew.com.project.madcourse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Andrew
 */
public class Exercise implements Serializable {

    private int mId = -1;
    private String mName;
    private String mQuantity;
    private int mRepetition;

    public Exercise (){

    }

    public Exercise (int id, String name, String quantity, int repetition){
        mId = id;
        mName = name;
        mQuantity = quantity;
        mRepetition = repetition;
    }

    public int getId() {
        return mId;
    }

    public boolean hasBeenSaved() { return (getId() != -1); }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public int getRepetition() {
        return mRepetition;
    }

    public void setRepetition(int repetition) {
        mRepetition = repetition;
    }
}
