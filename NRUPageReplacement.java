import java.util.*;

public class NRUPageReplacement {
    private int numFrames;
    private List<Integer> frameList;
    private List<Integer> usageList;

    public NRUPageReplacement(int numFrames) {
        this.numFrames = numFrames;
        this.frameList = new ArrayList<>();
        this.usageList = new ArrayList<>();
    }

    public int findPageToReplace(int NumPages, int[] pageRequests) {
        int pageFaults = 0;
        int pageHits = 0;
        float PFR, PHR;
        int replaceIndex = -1;

        for (int i = 0; i < NumPages; i++) {
            int page = pageRequests[i];
            if (frameList.contains(page)) {
                int index = frameList.indexOf(page);
                usageList.set(index, 1);
            } else {
                pageFaults++;
                if (frameList.size() < numFrames) {
                    frameList.add(page);
                    usageList.add(1);
                } else {
                    replaceIndex = getLowestUsagePageIndex();
                    frameList.set(replaceIndex, page);
                    usageList.set(replaceIndex, 1);
                }
            }
            System.out.printf("%-10d%-100s%-60s\n", i + 1, Arrays.toString(pageRequests), getMemoryState());
        }
        pageHits = NumPages - pageFaults;
        PFR = ((float)pageFaults / (float)NumPages) * 100;
        PHR = ((float)pageHits / (float)NumPages) * 100;
        System.out.println("\nPage Faults = " + pageFaults);
        System.out.println("Page Hits = " + pageHits);
        System.out.println("Page Faults Ratio = " + PFR + "%");
        System.out.println("Page Hits Ratio = " + PHR + "%");
        return 0;
    }

    private int getLowestUsagePageIndex() {
        int minIndex = -1;
        for (int i = 0; i < usageList.size(); i++) {
            if (usageList.get(i) == 0) {
                return i;
            }
            if (minIndex == -1 || usageList.get(i) < usageList.get(minIndex)) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    private String getMemoryState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numFrames; i++) {
            if (i < frameList.size()) {
                sb.append(frameList.get(i));
                sb.append("(");
                sb.append(usageList.get(i));
                sb.append(")");
            } else {
                sb.append(" -  ");
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of Pages (1-30): ");
        int NumPages = sc.nextInt();
        int[] pageRequests = new int[NumPages];
        System.out.println("Enter the Input Stream:");
        for (int i = 0; i < NumPages; i++) {
            System.out.print((i + 1) + ". ");
            pageRequests[i] = sc.nextInt();
        }
        System.out.print("Enter the number of Frames (1-10): ");
        int numFrames = sc.nextInt();
        sc.close();
        NRUPageReplacement nru = new NRUPageReplacement(numFrames);
        System.out.println("\nNRU Page Replacement Algorithm");
        System.out.printf("%-10s%-100s%-60s\n", "Step", "Page Request", "Memory State");
        System.out.printf("%-10s%-100s%-60s\n", "----", "------------", "------------");
        nru.findPageToReplace(NumPages, pageRequests);
    }
}