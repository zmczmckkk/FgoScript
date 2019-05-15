package fgoScript.entity.guda;

public class QpApGudaziForSpecial extends QpApGudazi{
    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception {
        ExpApGudaziForMainSpecial expGuda = new ExpApGudaziForMainSpecial();
        expGuda.setGatesDirPath(System.getProperty("user.dir") + "/config/special_all_qp.json");
        expGuda.insertIntoExpRoom(apNum);
    }
}
