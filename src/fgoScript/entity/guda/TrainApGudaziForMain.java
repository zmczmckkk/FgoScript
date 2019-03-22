package fgoScript.entity.guda;

public class TrainApGudaziForMain extends TrainApGudazi {
	@Override
	public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
		new EventGudazi().fightAndStop(rebootFlag, apNum);
	}
}
