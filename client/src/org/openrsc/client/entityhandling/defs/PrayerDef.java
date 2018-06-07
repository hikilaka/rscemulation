package org.openrsc.client.entityhandling.defs;

public class PrayerDef extends EntityDef {

    public int reqLevel;
    public int drainRate;

	public PrayerDef(int level, int drainRate, String name, String description) {
		super(name, description);
		this.reqLevel = level;
		this.drainRate = drainRate;
	}
    public int getReqLevel() {
        return reqLevel;
    }

    public int getDrainRate() {
        return drainRate;
    }
}
