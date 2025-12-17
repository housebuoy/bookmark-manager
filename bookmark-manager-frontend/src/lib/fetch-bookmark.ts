"use client";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { Bookmark } from "@/stores/bookmark-store";

export function useBookmarks(selectedTags: string[]) {
  return useQuery<Bookmark[]>({
    queryKey: ["bookmarks", selectedTags],
    queryFn: async () => {
      const url =
        selectedTags.length > 0
          ? `https://bookmark-manager-d117.onrender.com/api/bookmarks/by-tag?tag=${selectedTags.join(",")}`
          : "https://bookmark-manager-d117.onrender.com/api/bookmarks";

      const { data } = await axios.get(url);
      return data;
    },
  });
}
