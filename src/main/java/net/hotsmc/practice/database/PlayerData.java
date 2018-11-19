package net.hotsmc.practice.database;

import lombok.Getter;
import lombok.Setter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.MongoUtility;
import org.bson.Document;

import java.io.File;
import java.sql.Timestamp;

@Getter
@Setter
public class PlayerData {

    private String uuid;
    private String name;
    private Timestamp firstPlayed;
    private int point;

    private int unrankedPlayed;
    private int unraknedWin;
    private int rankedPlayed;
    private int rankedWin;
    private int nodebuffElo;
    private int debuffElo;
    private int mcsgElo;
    private int octcElo;
    private int gappleElo;
    private int archerElo;
    private int comboElo;
    private int soupElo;
    private int builduhcElo;
    private int sumoElo;
    private int axeElo;
    private int spleefElo;
    private int gapplesgElo;

    private int nodebuffRankedPlayed;
    private int debuffRankedPlayed;
    private int mcsgRankedPlayed;
    private int octcRankedPlayed;
    private int gappleRankedPlayed;
    private int archerRankedPlayed;
    private int comboRankedPlayed;
    private int soupRankedPlayed;
    private int builduhcRankedPlayed;
    private int sumoRankedPlayed;
    private int axeRankedPlayed;
    private int spleefRankedPlayed;
    private int gapplesgRankedPlayed;

    private int nodebuffUnankedPlayed;
    private int debuffUnankedPlayed;
    private int mcsgUnankedPlayed;
    private int octcUnankedPlayed;
    private int gappleUnankedPlayed;
    private int archerUnankedPlayed;
    private int comboUnankedPlayed;
    private int soupUnankedPlayed;
    private int builduhcUnankedPlayed;
    private int sumoUnankedPlayed;
    private int axeUnankedPlayed;
    private int spleefUnankedPlayed;
    private int gapplesgUnankedPlayed;

    private int nodebuffRankedWin;
    private int debuffRankedWin;
    private int mcsgRankedWin;
    private int octcRankedWin;
    private int gappleRankedWin;
    private int archerRankedWin;
    private int comboRankedWin;
    private int soupRankedWin;
    private int builduhcRankedWin;
    private int sumoRankedWin;
    private int axeRankedWin;
    private int spleefRankedWin;
    private int gapplesgRankedWin;

    private int nodebuffUnankedWin;
    private int debuffUnankedWin;
    private int mcsgUnankedWin;
    private int octcUnankedWin;
    private int gappleUnankedWin;
    private int archerUnankedWin;
    private int comboUnankedWin;
    private int soupUnankedWin;
    private int builduhcUnankedWin;
    private int sumoUnankedWin;
    private int axeUnankedWin;
    private int spleefUnankedWin;
    private int gapplesgUnankedWin;

    public PlayerData(String uuid) {
        this.uuid = uuid;
    }

    private static MongoConnection getMongoConnection() {
        return HotsPractice.getMongoConnection();
    }

    private Document findByUUID() {
        return getMongoConnection().getPlayers().find(MongoUtility.find("UUID", uuid)).first();
    }

    private Document findByName() {
        return getMongoConnection().getPlayers().find(MongoUtility.find("NAME", uuid)).first();
    }

    private void updateOne(Document updateDocument) {
        getMongoConnection().getPlayers().updateOne(findByUUID(), new Document("$set", updateDocument));
    }

    public void insertNewPlayerData() {
        Document document = new Document();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        document.put("UUID", uuid);
        document.put("NAME", name);
        document.put("FIRST_PLAYED", timestamp.getTime());

        document.put("POINT", 0);

        document.put("UNRANKED_PLAYED", 0);
        document.put("UNRANKED_WIN", 0);

        document.put("RANKED_PLAYED", 0);
        document.put("RANKED_WIN", 0);

        document.put("NODEBUFF_ELO", 1000);
        document.put("DEBUFF_ELO", 1000);
        document.put("MCSG_ELO", 1000);
        document.put("OCTC_ELO", 1000);
        document.put("GAPPLE_ELO", 1000);
        document.put("ARCHER_ELO", 1000);
        document.put("COMBO_ELO", 1000);
        document.put("SOUP_ELO", 1000);
        document.put("BUILDUHC_ELO", 1000);
        document.put("SUMO_ELO", 1000);
        document.put("AXE_ELO", 1000);
        document.put("SPLEEF_ELO", 1000);
        document.put("GAPPLESG_ELO", 1000);

        document.put("NODEBUFF_RANKED_PLAYED", 0);
        document.put("DEBUFF_RANKED_PLAYED", 0);
        document.put("MCSG_RANKED_PLAYED", 0);
        document.put("OCTC_RANKED_PLAYED", 0);
        document.put("GAPPLE_RANKED_PLAYED", 0);
        document.put("ARCHER_RANKED_PLAYED", 0);
        document.put("COMBO_RANKED_PLAYED", 0);
        document.put("SOUP_RANKED_PLAYED", 0);
        document.put("BUILDUHC_RANKED_PLAYED", 0);
        document.put("SUMO_RANKED_PLAYED", 0);
        document.put("AXE_RANKED_PLAYED", 0);
        document.put("SPLEEF_RANKED_PLAYED", 0);
        document.put("GAPPLESG_RANKED_PLAYED", 0);

        document.put("NODEBUFF_UNRANKED_PLAYED", 0);
        document.put("DEBUFF_UNRANKED_PLAYED", 0);
        document.put("MCSG_UNRANKED_PLAYED", 0);
        document.put("OCTC_UNRANKED_PLAYED", 0);
        document.put("GAPPLE_UNRANKED_PLAYED", 0);
        document.put("ARCHER_UNRANKED_PLAYED", 0);
        document.put("COMBO_UNRANKED_PLAYED", 0);
        document.put("SOUP_UNRANKED_PLAYED", 0);
        document.put("BUILDUHC_UNRANKED_PLAYED", 0);
        document.put("SUMO_UNRANKED_PLAYED", 0);
        document.put("AXE_UNRANKED_PLAYED", 0);
        document.put("SPLEEF_UNRANKED_PLAYED", 0);
        document.put("GAPPLESG_UNRANKED_PLAYED", 0);

        document.put("NODEBUFF_RANKED_WIN", 0);
        document.put("DEBUFF_RANKED_WIN", 0);
        document.put("MCSG_RANKED_WIN", 0);
        document.put("OCTC_RANKED_WIN", 0);
        document.put("GAPPLE_RANKED_WIN", 0);
        document.put("ARCHER_RANKED_WIN", 0);
        document.put("COMBO_RANKED_WIN", 0);
        document.put("SOUP_RANKED_WIN", 0);
        document.put("BUILDUHC_RANKED_WIN", 0);
        document.put("SUMO_RANKED_WIN", 0);
        document.put("AXE_RANKED_WIN", 0);
        document.put("SPLEEF_RANKED_WIN", 0);
        document.put("GAPPLESG_RANKED_WIN", 0);

        document.put("NODEBUFF_UNRANKED_WIN", 0);
        document.put("DEBUFF_UNRANKED_WIN", 0);
        document.put("MCSG_UNRANKED_WIN", 0);
        document.put("OCTC_UNRANKED_WIN", 0);
        document.put("GAPPLE_UNRANKED_WIN", 0);
        document.put("ARCHER_UNRANKED_WIN", 0);
        document.put("COMBO_UNRANKED_WIN", 0);
        document.put("SOUP_UNRANKED_WIN", 0);
        document.put("BUILDUHC_UNRANKED_WIN", 0);
        document.put("SUMO_UNRANKED_WIN", 0);
        document.put("AXE_UNRANKED_WIN", 0);
        document.put("SPLEEF_UNRANKED_WIN", 0);
        document.put("GAPPLESG_UNRANKED_WIN", 0);

        setFirstPlayed(timestamp);

        setPoint(document.getInteger("POINT"));

        setUnrankedPlayed(document.getInteger("UNRANKED_PLAYED"));
        setUnraknedWin(document.getInteger("UNRANKED_WIN"));

        setRankedPlayed(document.getInteger("RANKED_PLAYED"));
        setRankedWin(document.getInteger("RANKED_WIN"));

        setNodebuffElo(document.getInteger("NODEBUFF_ELO"));
        setDebuffElo(document.getInteger("DEBUFF_ELO"));
        setMcsgElo(document.getInteger("MCSG_ELO"));
        setOctcElo(document.getInteger("OCTC_ELO"));
        setGappleElo(document.getInteger("GAPPLE_ELO"));
        setArcherElo(document.getInteger("ARCHER_ELO"));
        setComboElo(document.getInteger("COMBO_ELO"));
        setSoupElo(document.getInteger("SOUP_ELO"));
        setBuilduhcElo(document.getInteger("BUILDUHC_ELO"));
        setSumoElo(document.getInteger("SUMO_ELO"));
        setAxeElo(document.getInteger("AXE_ELO"));
        setSpleefElo(document.getInteger("SPLEEF_ELO"));
        setGapplesgElo(document.getInteger("GAPPLESG_ELO"));

        //Ranked Played
        setNodebuffRankedPlayed(document.getInteger("NODEBUFF_RANKED_PLAYED"));
        setDebuffRankedPlayed(document.getInteger("DEBUFF_RANKED_PLAYED"));
        setMcsgRankedPlayed(document.getInteger("MCSG_RANKED_PLAYED"));
        setOctcRankedPlayed(document.getInteger("OCTC_RANKED_PLAYED"));
        setGappleRankedPlayed(document.getInteger("GAPPLE_RANKED_PLAYED"));
        setArcherRankedPlayed(document.getInteger("ARCHER_RANKED_PLAYED"));
        setComboRankedPlayed(document.getInteger("COMBO_RANKED_PLAYED"));
        setSoupRankedPlayed(document.getInteger("SOUP_RANKED_PLAYED"));
        setBuilduhcRankedPlayed(document.getInteger("BUILDUHC_RANKED_PLAYED"));
        setSumoRankedPlayed(document.getInteger("SUMO_RANKED_PLAYED"));
        setAxeRankedPlayed(document.getInteger("AXE_RANKED_PLAYED"));
        setSpleefRankedPlayed(document.getInteger("SPLEEF_RANKED_PLAYED"));
        setGapplesgRankedPlayed(document.getInteger("GAPPLESG_RANKED_PLAYED"));

        //Unranked Played
        setNodebuffUnankedPlayed(document.getInteger("NODEBUFF_UNRANKED_PLAYED"));
        setDebuffUnankedPlayed(document.getInteger("DEBUFF_RANKED_PLAYED"));
        setMcsgUnankedPlayed(document.getInteger("MCSG_UNRANKED_PLAYED"));
        setOctcUnankedPlayed(document.getInteger("OCTC_UNRANKED_PLAYED"));
        setGappleUnankedPlayed(document.getInteger("GAPPLE_UNRANKED_PLAYED"));
        setArcherUnankedPlayed(document.getInteger("ARCHER_UNRANKED_PLAYED"));
        setComboUnankedPlayed(document.getInteger("COMBO_UNRANKED_PLAYED"));
        setSoupUnankedPlayed(document.getInteger("SOUP_UNRANKED_PLAYED"));
        setBuilduhcUnankedPlayed(document.getInteger("BUILDUHC_UNRANKED_PLAYED"));
        setSumoUnankedPlayed(document.getInteger("SUMO_UNRANKED_PLAYED"));
        setAxeUnankedPlayed(document.getInteger("AXE_UNRANKED_PLAYED"));
        setSpleefUnankedPlayed(document.getInteger("SPLEEF_UNRANKED_PLAYED"));
        setGapplesgUnankedPlayed(document.getInteger("GAPPLESG_UNRANKED_PLAYED"));

        //Ranked Win
        setNodebuffRankedWin(document.getInteger("NODEBUFF_RANKED_WIN"));
        setDebuffRankedWin(document.getInteger("DEBUFF_RANKED_WIN"));
        setMcsgRankedWin(document.getInteger("MCSG_RANKED_WIN"));
        setOctcRankedWin(document.getInteger("OCTC_RANKED_WIN"));
        setGappleRankedWin(document.getInteger("GAPPLE_RANKED_WIN"));
        setArcherRankedWin(document.getInteger("ARCHER_RANKED_WIN"));
        setComboRankedWin(document.getInteger("COMBO_RANKED_WIN"));
        setSoupRankedWin(document.getInteger("SOUP_RANKED_WIN"));
        setBuilduhcRankedWin(document.getInteger("BUILDUHC_RANKED_WIN"));
        setSumoRankedWin(document.getInteger("SUMO_RANKED_WIN"));
        setAxeRankedWin(document.getInteger("AXE_RANKED_WIN"));
        setSpleefRankedWin(document.getInteger("SPLEEF_RANKED_WIN"));
        setGapplesgRankedWin(document.getInteger("GAPPLESG_RANKED_WIN"));

        //Unranked Win
        setNodebuffUnankedWin(document.getInteger("NODEBUFF_UNRANKED_WIN"));
        setDebuffUnankedWin(document.getInteger("DEBUFF_UNRANKED_WIN"));
        setMcsgUnankedWin(document.getInteger("MCSG_UNRANKED_WIN"));
        setOctcUnankedWin(document.getInteger("OCTC_UNRANKED_WIN"));
        setGappleUnankedWin(document.getInteger("GAPPLE_UNRANKED_WIN"));
        setArcherUnankedWin(document.getInteger("ARCHER_UNRANKED_WIN"));
        setComboUnankedWin(document.getInteger("COMBO_UNRANKED_WIN"));
        setSoupUnankedWin(document.getInteger("SOUP_UNRANKED_WIN"));
        setBuilduhcUnankedWin(document.getInteger("BUILDUHC_UNRANKED_WIN"));
        setSumoUnankedWin(document.getInteger("SUMO_UNRANKED_WIN"));
        setAxeUnankedWin(document.getInteger("AXE_UNRANKED_WIN"));
        setSpleefUnankedWin(document.getInteger("SPLEEF_UNRANKED_WIN"));
        setGapplesgUnankedWin(document.getInteger("GAPPLESG_UNRANKED_WIN"));

        getMongoConnection().getPlayers().insertOne(document);
    }

    public void loadData() {
        Document document = findByUUID();

        //なかったら新規作成
        if (document == null) {

            insertNewPlayerData();

            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/NoDebuff").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Debuff").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/MCSG").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/OCTC").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Gapple").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Archer").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Combo").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Soup").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/BuildUHC").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Sumo").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Axe").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/Spleef").mkdir();
            new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/GappleSG").mkdir();
        } else {

            //値取得
            setFirstPlayed(new Timestamp(document.getLong("FIRST_PLAYED")));

            setPoint(document.getInteger("POINT"));

            setUnrankedPlayed(document.getInteger("UNRANKED_PLAYED"));
            setUnraknedWin(document.getInteger("UNRANKED_WIN"));

            setRankedPlayed(document.getInteger("RANKED_PLAYED"));
            setRankedWin(document.getInteger("RANKED_WIN"));

            setNodebuffElo(document.getInteger("NODEBUFF_ELO"));
            setDebuffElo(document.getInteger("DEBUFF_ELO"));
            setMcsgElo(document.getInteger("MCSG_ELO"));
            setOctcElo(document.getInteger("OCTC_ELO"));
            setGappleElo(document.getInteger("GAPPLE_ELO"));
            setArcherElo(document.getInteger("ARCHER_ELO"));
            setComboElo(document.getInteger("COMBO_ELO"));
            setSoupElo(document.getInteger("SOUP_ELO"));
            setBuilduhcElo(document.getInteger("BUILDUHC_ELO"));
            setSumoElo(document.getInteger("SUMO_ELO"));
            setAxeElo(document.getInteger("AXE_ELO"));
            setSpleefElo(document.getInteger("SPLEEF_ELO"));
            setGapplesgElo(document.getInteger("GAPPLESG_ELO"));

            setNodebuffRankedPlayed(document.getInteger("NODEBUFF_RANKED_PLAYED"));
            setDebuffRankedPlayed(document.getInteger("DEBUFF_RANKED_PLAYED"));
            setMcsgRankedPlayed(document.getInteger("MCSG_RANKED_PLAYED"));
            setOctcRankedPlayed(document.getInteger("OCTC_RANKED_PLAYED"));
            setGappleRankedPlayed(document.getInteger("GAPPLE_RANKED_PLAYED"));
            setArcherRankedPlayed(document.getInteger("ARCHER_RANKED_PLAYED"));
            setComboRankedPlayed(document.getInteger("COMBO_RANKED_PLAYED"));
            setSoupRankedPlayed(document.getInteger("SOUP_RANKED_PLAYED"));
            setBuilduhcRankedPlayed(document.getInteger("BUILDUHC_RANKED_PLAYED"));
            setSumoRankedPlayed(document.getInteger("SUMO_RANKED_PLAYED"));
            setAxeRankedPlayed(document.getInteger("AXE_RANKED_PLAYED"));
            setSpleefRankedPlayed(document.getInteger("SPLEEF_RANKED_PLAYED"));
            setGapplesgRankedPlayed(document.getInteger("GAPPLESG_RANKED_PLAYED"));

            setNodebuffUnankedPlayed(document.getInteger("NODEBUFF_UNRANKED_PLAYED"));
            setDebuffUnankedPlayed(document.getInteger("DEBUFF_UNRANKED_PLAYED"));
            setMcsgUnankedPlayed(document.getInteger("MCSG_UNRANKED_PLAYED"));
            setOctcUnankedPlayed(document.getInteger("OCTC_UNRANKED_PLAYED"));
            setGappleUnankedPlayed(document.getInteger("GAPPLE_UNRANKED_PLAYED"));
            setArcherUnankedPlayed(document.getInteger("ARCHER_UNRANKED_PLAYED"));
            setComboUnankedPlayed(document.getInteger("COMBO_UNRANKED_PLAYED"));
            setSoupUnankedPlayed(document.getInteger("SOUP_UNRANKED_PLAYED"));
            setBuilduhcUnankedPlayed(document.getInteger("BUILDUHC_UNRANKED_PLAYED"));
            setSumoUnankedPlayed(document.getInteger("SUMO_UNRANKED_PLAYED"));
            setAxeUnankedPlayed(document.getInteger("AXE_UNRANKED_PLAYED"));
            setSpleefUnankedPlayed(document.getInteger("SPLEEF_UNRANKED_PLAYED"));
            setGapplesgUnankedPlayed(document.getInteger("GAPPLESG_UNRANKED_PLAYED"));

            setNodebuffRankedWin(document.getInteger("NODEBUFF_RANKED_WIN"));
            setDebuffRankedWin(document.getInteger("DEBUFF_RANKED_WIN"));
            setMcsgRankedWin(document.getInteger("MCSG_RANKED_WIN"));
            setOctcRankedWin(document.getInteger("OCTC_RANKED_WIN"));
            setGappleRankedWin(document.getInteger("GAPPLE_RANKED_WIN"));
            setArcherRankedWin(document.getInteger("ARCHER_RANKED_WIN"));
            setComboRankedWin(document.getInteger("COMBO_RANKED_WIN"));
            setSoupRankedWin(document.getInteger("SOUP_RANKED_WIN"));
            setBuilduhcRankedWin(document.getInteger("BUILDUHC_RANKED_WIN"));
            setSumoRankedWin(document.getInteger("SUMO_RANKED_WIN"));
            setAxeRankedWin(document.getInteger("AXE_RANKED_WIN"));
            setSpleefRankedWin(document.getInteger("SPLEEF_RANKED_WIN"));
            setGapplesgRankedWin(document.getInteger("GAPPLESG_RANKED_WIN"));

            setNodebuffUnankedWin(document.getInteger("NODEBUFF_UNRANKED_WIN"));
            setDebuffUnankedWin(document.getInteger("DEBUFF_UNRANKED_WIN"));
            setMcsgUnankedWin(document.getInteger("MCSG_UNRANKED_WIN"));
            setOctcUnankedWin(document.getInteger("OCTC_UNRANKED_WIN"));
            setGappleUnankedWin(document.getInteger("GAPPLE_UNRANKED_WIN"));
            setArcherUnankedWin(document.getInteger("ARCHER_UNRANKED_WIN"));
            setComboUnankedWin(document.getInteger("COMBO_UNRANKED_WIN"));
            setSoupUnankedWin(document.getInteger("SOUP_UNRANKED_WIN"));
            setBuilduhcUnankedWin(document.getInteger("BUILDUHC_UNRANKED_WIN"));
            setSumoUnankedWin(document.getInteger("SUMO_UNRANKED_WIN"));
            setAxeUnankedWin(document.getInteger("AXE_UNRANKED_WIN"));
            setSpleefUnankedWin(document.getInteger("SPLEEF_UNRANKED_WIN"));
            setGapplesgUnankedWin(document.getInteger("GAPPLESG_UNRANKED_WIN"));

            //データベースに登録されている名前と違ったら更新
            if (!name.equals(document.getString("NAME"))) {
                updateName();
            }
        }
    }

    private void updateName() {
        updateOne(findByUUID().append("NAME", name));
    }

    private void updateInteger(String key, int amount) {
        updateOne(findByUUID().append(key, amount));
    }

    public void addPoint() {
        updateInteger("POINT", getPoint() + 2);
        setPoint(getPoint() + 2);
    }

    public void withdrawPoint() {
        if (getPoint() >= 1) {
            updateInteger("POINT", getPoint() - 1);
            setPoint(getPoint() - 1);
        }
    }

    public void addTotalPlayCount(RankedType rankedType) {
        if (rankedType == RankedType.UNRANKED) {
            updateInteger("UNRANKED_PLAYED", getUnrankedPlayed() + 1);
            setUnrankedPlayed(getUnrankedPlayed() + 1);
        }
        if (rankedType == RankedType.RANKED) {
            updateInteger("RANKED_PLAYED", getRankedPlayed() + 1);
            setRankedPlayed(getRankedPlayed() + 1);
        }
    }

    public void addTotalWinCount(RankedType rankedType) {
        if (rankedType == RankedType.UNRANKED) {
            updateInteger("UNRANKED_WIN", getUnraknedWin() + 1);
            setUnraknedWin(getUnraknedWin() + 1);
        }
        if (rankedType == RankedType.RANKED) {
            updateInteger("RANKED_WIN", getRankedWin() + 1);
            setRankedWin(getRankedWin() + 1);
        }
    }

    public void addPlayCount(RankedType rankedType, KitType kitType) {
        if (rankedType == RankedType.UNRANKED) {
            if (kitType == KitType.NoDebuff) {
                updateInteger("NODEBUFF_UNRANKED_PLAYED", getNodebuffUnankedPlayed() + 1);
                setNodebuffUnankedPlayed(getNodebuffUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Debuff) {
                updateInteger("DEBUFF_UNRANKED_PLAYED", getDebuffUnankedPlayed() + 1);
                setDebuffUnankedPlayed(getDebuffUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.MCSG) {
                updateInteger("MCSG_UNRANKED_PLAYED", getMcsgUnankedPlayed() + 1);
                setMcsgUnankedPlayed(getMcsgUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.OCTC) {
                updateInteger("OCTC_UNRANKED_PLAYED", getOctcUnankedPlayed() + 1);
                setOctcUnankedPlayed(getOctcUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Gapple) {
                updateInteger("GAPPLE_UNRANKED_PLAYED", getGappleUnankedPlayed() + 1);
                setGappleUnankedPlayed(getGappleUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Archer) {
                updateInteger("ARCHER_UNRANKED_PLAYED", getArcherUnankedPlayed() + 1);
                setArcherUnankedPlayed(getArcherUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Combo) {
                updateInteger("COMBO_UNRANKED_PLAYED", getComboUnankedPlayed() + 1);
                setComboUnankedPlayed(getComboUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Soup) {
                updateInteger("SOUP_UNRANKED_PLAYED", getSoupUnankedPlayed() + 1);
                setSoupUnankedPlayed(getSoupUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.BuildUHC) {
                updateInteger("BUILDUHC_UNRANKED_PLAYED", getBuilduhcUnankedPlayed() + 1);
                setBuilduhcUnankedPlayed(getBuilduhcUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Sumo) {
                updateInteger("SUMO_UNRANKED_PLAYED", getSumoUnankedPlayed() + 1);
                setSumoUnankedPlayed(getSumoUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Axe) {
                updateInteger("AXE_UNRANKED_PLAYED", getAxeUnankedPlayed() + 1);
                setAxeUnankedPlayed(getAxeUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Spleef) {
                updateInteger("SPLEEF_UNRANKED_PLAYED", getSpleefUnankedPlayed() + 1);
                setSpleefUnankedPlayed(getSpleefUnankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.GappleSG) {
                updateInteger("GAPPLESG_UNRANKED_PLAYED", getGapplesgUnankedPlayed() + 1);
                setGapplesgUnankedPlayed(getGapplesgUnankedPlayed() + 1);
                return;
            }
        }
        if (rankedType == RankedType.RANKED) {
            if (kitType == KitType.NoDebuff) {
                updateInteger("NODEBUFF_RANKED_PLAYED", getNodebuffRankedPlayed() + 1);
                setNodebuffRankedPlayed(getNodebuffRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Debuff) {
                updateInteger("DEBUFF_RANKED_PLAYED", getDebuffRankedPlayed() + 1);
                setDebuffRankedPlayed(getDebuffRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.MCSG) {
                updateInteger("MCSG_RANKED_PLAYED", getMcsgRankedPlayed() + 1);
                setMcsgRankedPlayed(getMcsgRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.OCTC) {
                updateInteger("OCTC_RANKED_PLAYED", getOctcRankedPlayed() + 1);
                setOctcRankedPlayed(getOctcRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Gapple) {
                updateInteger("GAPPLE_RANKED_PLAYED", getGappleRankedPlayed() + 1);
                setGappleRankedPlayed(getGappleRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Archer) {
                updateInteger("ARCHER_RANKED_PLAYED", getArcherRankedPlayed() + 1);
                setArcherRankedPlayed(getArcherRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Combo) {
                updateInteger("COMBO_RANKED_PLAYED", getComboRankedPlayed() + 1);
                setComboRankedPlayed(getComboRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Soup) {
                updateInteger("SOUP_RANKED_PLAYED", getSoupRankedPlayed() + 1);
                setSoupRankedPlayed(getSoupRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.BuildUHC) {
                updateInteger("BUILDUHC_RANKED_PLAYED", getBuilduhcRankedPlayed() + 1);
                setBuilduhcRankedPlayed(getBuilduhcRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Sumo) {
                updateInteger("SUMO_RANKED_PLAYED", getSumoRankedPlayed() + 1);
                setSumoRankedPlayed(getSumoRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Axe) {
                updateInteger("AXE_RANKED_PLAYED", getAxeRankedPlayed() + 1);
                setAxeRankedPlayed(getAxeRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.Spleef) {
                updateInteger("SPLEEF_RANKED_PLAYED", getSpleefRankedPlayed() + 1);
                setSpleefRankedPlayed(getSpleefRankedPlayed() + 1);
                return;
            }
            if (kitType == KitType.GappleSG) {
                updateInteger("GAPPLESG_RANKED_PLAYED", getGapplesgRankedPlayed() + 1);
                setGapplesgRankedPlayed(getGapplesgRankedPlayed() + 1);
            }
        }
    }

    public void addWinCount(RankedType rankedType, KitType kitType) {
        if (rankedType == RankedType.UNRANKED) {
            if (kitType == KitType.NoDebuff) {
                updateInteger("NODEBUFF_UNRANKED_WIN", getNodebuffUnankedWin() + 1);
                setNodebuffUnankedWin(getNodebuffUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Debuff) {
                updateInteger("DEBUFF_UNRANKED_WIN", getDebuffUnankedWin() + 1);
                setDebuffUnankedWin(getDebuffUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.MCSG) {
                updateInteger("MCSG_UNRANKED_WIN", getMcsgUnankedWin() + 1);
                setMcsgUnankedWin(getMcsgUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.OCTC) {
                updateInteger("OCTC_UNRANKED_WIN", getOctcUnankedWin() + 1);
                setOctcUnankedWin(getOctcUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Gapple) {
                updateInteger("GAPPLE_UNRANKED_WIN", getGappleUnankedWin() + 1);
                setGappleUnankedWin(getGappleUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Archer) {
                updateInteger("ARCHER_UNRANKED_WIN", getArcherUnankedWin() + 1);
                setArcherUnankedWin(getArcherUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Combo) {
                updateInteger("COMBO_UNRANKED_WIN", getComboUnankedWin() + 1);
                setComboUnankedWin(getComboUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Soup) {
                updateInteger("SOUP_UNRANKED_WIN", getSoupUnankedWin() + 1);
                setSoupUnankedWin(getSoupUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.BuildUHC) {
                updateInteger("BUILDUHC_UNRANKED_WIN", getBuilduhcUnankedWin() + 1);
                setBuilduhcUnankedWin(getBuilduhcUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Sumo) {
                updateInteger("SUMO_UNRANKED_WIN", getSumoUnankedWin() + 1);
                setSumoUnankedWin(getSumoUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Axe) {
                updateInteger("AXE_UNRANKED_WIN", getAxeUnankedWin() + 1);
                setAxeUnankedWin(getAxeUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.Spleef) {
                updateInteger("SPLEEF_UNRANKED_WIN", getSpleefUnankedWin() + 1);
                setSpleefUnankedWin(getSpleefUnankedWin() + 1);
                return;
            }
            if (kitType == KitType.GappleSG) {
                updateInteger("GAPPLESG_UNRANKED_WIN", getGapplesgUnankedWin() + 1);
                setGapplesgUnankedWin(getGapplesgUnankedWin() + 1);
                return;
            }
        }
        if (rankedType == RankedType.RANKED) {
            if (kitType == KitType.NoDebuff) {
                updateInteger("NODEBUFF_RANKED_WIN", getNodebuffRankedWin() + 1);
                setNodebuffRankedWin(getNodebuffRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Debuff) {
                updateInteger("DEBUFF_RANKED_WIN", getDebuffRankedWin() + 1);
                setDebuffRankedWin(getDebuffRankedWin() + 1);
                return;
            }
            if (kitType == KitType.MCSG) {
                updateInteger("MCSG_RANKED_WIN", getMcsgRankedWin() + 1);
                setMcsgRankedWin(getMcsgRankedWin() + 1);
                return;
            }
            if (kitType == KitType.OCTC) {
                updateInteger("OCTC_RANKED_WIN", getOctcRankedWin() + 1);
                setOctcRankedWin(getOctcRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Gapple) {
                updateInteger("GAPPLE_RANKED_WIN", getGappleRankedWin() + 1);
                setGappleRankedWin(getGappleRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Archer) {
                updateInteger("ARCHER_RANKED_WIN", getArcherRankedWin() + 1);
                setArcherRankedWin(getArcherRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Combo) {
                updateInteger("COMBO_RANKED_WIN", getComboRankedWin() + 1);
                setComboRankedWin(getComboRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Soup) {
                updateInteger("SOUP_RANKED_WIN", getSoupRankedWin() + 1);
                setSoupRankedWin(getSoupRankedWin() + 1);
                return;
            }
            if (kitType == KitType.BuildUHC) {
                updateInteger("BUILDUHC_RANKED_WIN", getBuilduhcRankedWin() + 1);
                setBuilduhcRankedWin(getBuilduhcRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Sumo) {
                updateInteger("SUMO_RANKED_WIN", getSumoRankedWin() + 1);
                setSumoRankedWin(getSumoRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Axe) {
                updateInteger("AXE_RANKED_WIN", getAxeRankedWin() + 1);
                setAxeRankedWin(getAxeRankedWin() + 1);
                return;
            }
            if (kitType == KitType.Spleef) {
                updateInteger("SPLEEF_RANKED_WIN", getSpleefRankedWin() + 1);
                setSpleefRankedWin(getSpleefRankedWin() + 1);
                return;
            }
            if (kitType == KitType.GappleSG) {
                updateInteger("GAPPLESG_RANKED_WIN", getGapplesgRankedWin() + 1);
                setGapplesgRankedWin(getGapplesgRankedWin() + 1);
            }
        }
    }

    public int calculatedElo(KitType kitType) {
        return (int) (getElo(kitType) * 0.01);
    }

    public void updateElo(KitType kitType, int elo) {
        if (kitType == KitType.NoDebuff) {
            setNodebuffElo(elo);
            updateInteger("NODEBUFF_ELO", elo);
            return;
        }
        if (kitType == KitType.Debuff) {
            setDebuffElo(elo);
            updateInteger("DEBUFF_ELO", elo);
            return;
        }
        if (kitType == KitType.MCSG) {
            setMcsgElo(elo);
            updateInteger("MCSG_ELO", elo);
            return;
        }
        if (kitType == KitType.OCTC) {
            setOctcElo(elo);
            updateInteger("OCTC_ELO", elo);
            return;
        }
        if (kitType == KitType.Gapple) {
            setGappleElo(elo);
            updateInteger("GAPPLE_ELO", elo);
            return;
        }
        if (kitType == KitType.Archer) {
            setArcherElo(elo);
            updateInteger("ARCHER_ELO", elo);
            return;
        }
        if (kitType == KitType.Combo) {
            setComboElo(elo);
            updateInteger("COMBO_ELO", elo);
            return;
        }
        if (kitType == KitType.Soup) {
            setSoupElo(elo);
            updateInteger("SOUP_ELO", elo);
            return;
        }
        if (kitType == KitType.BuildUHC) {
            setBuilduhcElo(elo);
            updateInteger("BUILDUHC_ELO", elo);
            return;
        }
        if (kitType == KitType.Sumo) {
            setSumoElo(elo);
            updateInteger("SUMO_ELO", elo);
            return;
        }
        if (kitType == KitType.Axe) {
            setAxeElo(elo);
            updateInteger("AXE_ELO", elo);
            return;
        }
        if (kitType == KitType.Spleef) {
            setSpleefElo(elo);
            updateInteger("SPLEEF_ELO", elo);
            return;
        }
        if (kitType == KitType.GappleSG) {
            setGapplesgElo(elo);
            updateInteger("GAPPLESG_ELO", elo);
        }
    }

    public int getElo(KitType kitType){
        if (kitType == KitType.NoDebuff) {
            return nodebuffElo;
        }
        if (kitType == KitType.Debuff) {
            return debuffElo;
        }
        if (kitType == KitType.MCSG) {
            return mcsgElo;
        }
        if (kitType == KitType.OCTC) {
            return octcElo;
        }
        if (kitType == KitType.Gapple) {
            return gappleElo;
        }
        if (kitType == KitType.Archer) {
            return archerElo;
        }
        if (kitType == KitType.Combo) {
            return comboElo;
        }
        if (kitType == KitType.Soup) {
            return soupElo;
        }
        if (kitType == KitType.BuildUHC) {
            return builduhcElo;
        }
        if (kitType == KitType.Sumo) {
            return sumoElo;
        }
        if (kitType == KitType.Axe) {
           return axeElo;
        }
        if (kitType == KitType.Spleef) {
            return spleefElo;
        }
        if (kitType == KitType.GappleSG) {
           return gapplesgElo;
        }
        return 0;
    }
}
