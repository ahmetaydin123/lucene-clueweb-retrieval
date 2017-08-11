package org.clueweb09.tracks;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 2010 Web Track topics (query field only)
 * http://trec.nist.gov/data/web/10/wt2010-topics.queries-only
 */
public class WT10 extends Track {

    public WT10(String home) {
        super(home);
    }

    @Override
    protected void populateInfoNeeds() throws IOException {
        populateInfoNeedsWT(Paths.get(home, "topics-and-qrels", "topics.web.51-100.txt"));
    }

    @Override
    protected void populateQRelsMap() throws Exception {
        populateQRelsMap(Paths.get(home, "topics-and-qrels", "qrels.web.51-100.txt"));
    }

    private static final String[] wt10 = {

            "51:horse hooves",
            "52:avp",
            "53:discovery channel store",
            "54:president of the united states",
            "55:iron",
            "56:uss yorktown charleston sc",
            "57:ct jobs",
            "58:penguins",
            "59:how to build a fence",
            "60:bellevue",
            "61:worm",
            "62:texas border patrol",
            "63:flushing",
            "64:moths",
            "65:korean language",
            "66:income tax return online",
            "67:vldl levels",
            "68:pvc",
            "69:sewing instructions",
            "70:to be or not to be that is the question",
            "71:living in india",
            "72:the sun",
            "73:neil young",
            "74:kiwi",
            "75:tornadoes",
            "76:raised gardens",
            "77:bobcat",
            "78:dieting",
            "79:voyager",
            "80:keyboard reviews",
            "81:afghanistan",
            "82:joints",
            "83:memory",
            "84:continental plates",
            "85:milwaukee journal sentinel",
            "86:bart sf",
            "87:who invented music",
            "88:forearm pain",
            "89:ocd",
            "90:mgb",
            "91:er tv show",
            "92:the wall",
            "93:raffles",
            "94:titan",
            //"95:earn money at home",
            "96:rice",
            "97:south africa",
            "98:sat",
            "99:satellite",
            //"100:rincon puerto rico"
    };

}
